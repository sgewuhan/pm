package com.sg.business.work.view;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.internal.provisional.action.ToolBarManager2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.portal.user.IAccountEvent;
import com.sg.business.model.IModelConstants;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.business.work.filter.WorkFilterControl;
import com.sg.business.work.nls.Messages;
//import com.sg.widgets.ImageResource;
import com.sg.widgets.MessageUtil;
//import com.sg.widgets.Widgets;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

@SuppressWarnings("restriction")
public class WorkInProcess extends AccountSensitiveTreeView {

	class ShowFilterMenuAction extends Action {

		public ShowFilterMenuAction() {
			setId("work.showFilterMenu"); //$NON-NLS-1$
			setText(Messages.get().WorkInProcess_1);
//			setImageDescriptor(Widgets
//					.getImageDescriptor(ImageResource.FILTER_24));
		}

		@Override
		public void run() {
			ToolBarManager2 manager = (ToolBarManager2) getViewSite().getActionBars().getToolBarManager();
			ToolBar control = manager.getControl();
			int index = manager.indexOf(this.getId());

			Menu menu = menuManager.createContextMenu(control);
			
			Point hl = control.toDisplay(0, 0);
			hl.y += control.getBounds().height + 2;
			hl.x += index*32+4;
					
			menu.setLocation(hl);
			menu.setVisible(true);
		}
	}

	private WorkflowSynchronizer workSynchronizer;
	private JobChangeAdapter syncUserListener;

	private WorkflowSynchronizer repeatWorkSynchronizer;
	private boolean processSync = false;
	private JobChangeAdapter syncBackgroundListener;
	private MenuManager menuManager;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		createWorkSynchronizer();
	}
	
	@Override
	protected void createToolbarItem(IToolBarManager manager) {
		WorkFilterControl fc = new WorkFilterControl(getNavigator().getViewer());
		menuManager = fc.createMenu();
		
		manager.add(new ShowFilterMenuAction());
		
		super.createToolbarItem(manager);

	}

	private void createWorkSynchronizer() {
		// 刷新工作流信息
		IContext context = getNavigator().getContext();
		String userid = context.getAccountInfo().getConsignerId();
		workSynchronizer = createUserSynchronizer(userid);
		doRefresh();

		String _interval = (String) Setting.getUserSetting(context
				.getAccountInfo().getUserId(),
				IModelConstants.S_U_WORK_RESERVED_REFRESH_INTERVAL);
		if (_interval != null) {
			Integer interval = Utils.getIntegerValue(_interval);
			if (interval != null) {
				repeatWorkSynchronizer = createBackgroundSynchronizer(userid);
				repeatWorkSynchronizer.start(interval.intValue() * 60 * 1000,
						false);
			}
		}
	}

	private WorkflowSynchronizer createUserSynchronizer(String userid) {
		WorkflowSynchronizer workSynchronizer = new WorkflowSynchronizer(true);
		workSynchronizer.setUserId(userid);

		final Display display = getSite().getShell().getDisplay();
		syncUserListener = new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
//						ViewerFilter[] filters = getNavigator().getViewer().getFilters();
						WorkInProcess.super.doRefresh();
						processSync = false;
						getNavigator().getViewer().resetFilters();
//						getNavigator().getViewer().setFilters(filters);
						
						// System.out.println("refresh by: User");
					}
				});
			}

		};
		workSynchronizer.addJobChangeListener(syncUserListener);
		return workSynchronizer;
	}

	private WorkflowSynchronizer createBackgroundSynchronizer(String userid) {
		WorkflowSynchronizer workSynchronizer = new WorkflowSynchronizer(true);
		workSynchronizer.setUserId(userid);

		final Display display = getSite().getShell().getDisplay();
		syncBackgroundListener = new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						WorkInProcess.super.doRefresh();
						processSync = false;
						getNavigator().getViewer().resetFilters();

						// System.out.println("refresh by: Background");
					}
				});
			}

		};
		workSynchronizer.addJobChangeListener(syncBackgroundListener);
		return workSynchronizer;
	}

	@Override
	public void doRefresh() {
		if (processSync) {
			MessageUtil.showToast(null, getPartName(), Messages.get().WorkInProcess_2,
					SWT.ICON_INFORMATION);
			return;
		}
		workSynchronizer.schedule();
		processSync = true;
	}

	@Override
	public void dispose() {
		workSynchronizer.removeJobChangeListener(syncUserListener);
		repeatWorkSynchronizer.removeJobChangeListener(syncBackgroundListener);
		repeatWorkSynchronizer.stop();
		menuManager.dispose();
		super.dispose();
	}

	@Override
	public void accountChanged(final IAccountEvent event) {
		if (IAccountEvent.EVENT_PROCESS_START.equals(event.getEventCode())
				|| IAccountEvent.EVENT_CONSIGNER_CHANGED.equals(event
						.getEventCode())) {
			super.accountChanged(event);
		}
	}

	@Override
	protected String getAccountNoticeText() {
		return Messages.get().WorkInProcess_3;
	}

	protected String getAccountNoticeMessage() {
		return Messages.get().WorkInProcess_4;
	}

}
