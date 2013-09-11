package com.sg.business.work.view;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mobnut.db.model.IContext;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.part.view.TreeNavigator;

public class WorkInProcess extends TreeNavigator {

	private WorkflowSynchronizer workSynchronizer;
	private JobChangeAdapter syncListener;

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		
		// 刷新工作流信息
		IContext context = getNavigator().getContext();
		String userid = context.getAccountInfo().getconsignerId();
		workSynchronizer = new WorkflowSynchronizer(true);
		workSynchronizer.setUser(true);
		workSynchronizer.setUserId(userid);

		final Display display = getSite().getShell().getDisplay();
		syncListener = new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						WorkInProcess.super.doRefresh();
					}
				});
			}
		};
		workSynchronizer.addJobChangeListener(syncListener);

	}

	@Override
	public void doRefresh() {
		workSynchronizer.schedule();
	}

	@Override
	public void dispose() {
		workSynchronizer.removeJobChangeListener(syncListener);
		super.dispose();
	}

}
