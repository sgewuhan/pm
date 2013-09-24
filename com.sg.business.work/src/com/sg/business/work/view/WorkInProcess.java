package com.sg.business.work.view;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mobnut.admin.dataset.Setting;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.portal.user.IAccountEvent;
import com.sg.business.model.IModelConstants;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.view.AccountSensitiveTreeView;

public class WorkInProcess extends AccountSensitiveTreeView {

	private WorkflowSynchronizer workSynchronizer;
	private JobChangeAdapter syncUserListener;

	private WorkflowSynchronizer repeatWorkSynchronizer;
	private boolean processSync = false;
	private JobChangeAdapter syncBackgroundListener;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

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
				repeatWorkSynchronizer.start(interval.intValue() *60 * 1000, false);
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
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						WorkInProcess.super.doRefresh();
						processSync = false;
//						System.out.println("refresh by: User");
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
//						System.out.println("refresh by: Background");
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
			MessageUtil.showToast(null, getPartName(), "请稍候, 正在处理更新。",
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
