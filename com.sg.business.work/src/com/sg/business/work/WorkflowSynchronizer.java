package com.sg.business.work;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;

import com.mobnut.commons.job.RepeatJob;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.dataset.organization.UserDataSetFactory;

public class WorkflowSynchronizer extends RepeatJob {

	private String userId;
	private boolean client;

	public WorkflowSynchronizer(boolean client) {
		super("更新流程信息");
		this.client = client;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		if(client){
			monitor.beginTask("正在同步工作流任务...", IProgressMonitor.UNKNOWN);
		}
		if (userId != null) {
			synchronizeUserTask(userId);
		} else {
			// 1. 取出用户
			UserDataSetFactory dsf = new UserDataSetFactory();
			dsf.setProjection(new BasicDBObject().append(User.F_USER_ID, 1));
			DataSet ds = dsf.getDataSet();
			Iterator<PrimaryObject> iter = ds.iterator();

			// 2. 遍历用户，将工作流任务写入到工作上
			while (iter.hasNext()) {
				PrimaryObject po = iter.next();
				String userid = (String) po.getValue(User.F_USER_ID);
				synchronizeUserTask(userid);
			}
		}

		return Status.OK_STATUS;
	}

	public Set<Work> synchronizeUserTask(String userid) {
		Set<Work> updated = new HashSet<Work>();
		Task[] tasks = WorkflowService.getDefault().getUserTasks(userid);
		for (int i = 0; i < tasks.length; i++) {
			TaskData taskData = tasks[i].getTaskData();

			long instanceId = taskData.getProcessInstanceId();
			String processId = taskData.getProcessId();

			try {
				Workflow workflow = new Workflow(processId, instanceId);
				PrimaryObject host = workflow.getHost();
				String flowKey = workflow.getKey();
				if (flowKey != null && host instanceof Work) {
					Work work = (Work) host;
					work.reload();
					work.doUpdateWorkflowDataByTask(flowKey, tasks[i], userid);
					
//					UserSessionContext.noticeAccountChanged(userid, new AccountEvent(
//							IAccountEvent.EVENT_PROCESS_TASKUPDATEED, work));
					
					updated.add(work);
				}
			} catch (Exception e) {
				//流程不存在
				//work被删除
				e.printStackTrace();
			}
		}
		return updated;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
