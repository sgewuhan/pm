package com.sg.business.work;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;

import com.mobnut.commons.job.RepeatJob;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.dataset.organization.UserDataSetFactory;
import com.sg.widgets.part.BackgroundContext;

public class WorkflowSynchronizer extends RepeatJob {

	private IContext context;

	public WorkflowSynchronizer() {
		super("Work Synchronizer");
		context = new BackgroundContext();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// 1. 取出用户
		UserDataSetFactory dsf = new UserDataSetFactory();
		dsf.setProjection(new BasicDBObject().append(User.F_USER_ID, 1));
		DataSet ds = dsf.getDataSet();
		Iterator<PrimaryObject> iter = ds.iterator();

		// 2. 遍历用户，将工作流任务写入到工作上
		while (iter.hasNext()) {
			PrimaryObject po = iter.next();
			String userid = (String) po.getValue(User.F_USER_ID);
			Task[] tasks = WorkflowService.getDefault().getTask(userid);
			for (int i = 0; i < tasks.length; i++) {
				TaskData taskData = tasks[i].getTaskData();

				long processInstanceId = taskData.getProcessInstanceId();
				String processId = taskData.getProcessId();

				try {
					Workflow workflow = new Workflow(processId,
							processInstanceId);
					PrimaryObject host = workflow.getHost();
					String flowKey = workflow.getKey();
					if (flowKey != null && host instanceof Work) {
						Work work = (Work) host;
						work.doUpdateWorkflowTask(flowKey, tasks[i], context);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return Status.OK_STATUS;
	}

}
