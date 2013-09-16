package com.sg.business.work.view;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mobnut.db.model.IContext;
import com.mobnut.portal.user.IAccountEvent;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class WorkInProcess extends AccountSensitiveTreeView {

	private WorkflowSynchronizer workSynchronizer;
	private JobChangeAdapter syncListener;
	private boolean processSync = false;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		// 刷新工作流信息
		IContext context = getNavigator().getContext();
		String userid = context.getAccountInfo().getConsignerId();
		workSynchronizer = new WorkflowSynchronizer(true);
		workSynchronizer.setUserId(userid);

		final Display display = getSite().getShell().getDisplay();
		syncListener = new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						WorkInProcess.super.doRefresh();
						processSync = false;
					}
				});
			}

		};
		workSynchronizer.addJobChangeListener(syncListener);
		doRefresh();
	}

	@Override
	public void doRefresh() {
		if (processSync) {
			MessageUtil.showToast(null,getPartName(),"请稍候, 正在处理更新", SWT.ICON_INFORMATION);
			return;
		}
		workSynchronizer.schedule();
		processSync = true;
	}

	@Override
	public void dispose() {
		workSynchronizer.removeJobChangeListener(syncListener);
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
		return "流程信息更新";
	}

	protected String getAccountNoticeMessage() {
		return "正在重新获取流程信息...";
	}

}
