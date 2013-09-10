package com.sg.business.work;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jbpm.task.Task;

import com.mobnut.commons.job.RepeatJob;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.business.model.User;
import com.sg.business.model.dataset.organization.UserDataSetFactory;

public class WorkflowSynchronizer extends RepeatJob {

	public WorkflowSynchronizer() {
		super("Work Synchronizer");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		//1. ȡ���û�
		UserDataSetFactory dsf = new UserDataSetFactory();
		dsf.setProjection(new BasicDBObject().append(User.F_USER_ID, 1));
		DataSet ds = dsf.getDataSet();
		Iterator<PrimaryObject> iter = ds.iterator();
		
		//2. �����û���������������д�뵽������
		while(iter.hasNext()){
			PrimaryObject po = iter.next();
			String userid = (String) po.getValue(User.F_USER_ID);
			List<Task> tasks = WorkflowService.getDefault().getTask(userid);
			
		}
		
		return Status.OK_STATUS;
	}

}
