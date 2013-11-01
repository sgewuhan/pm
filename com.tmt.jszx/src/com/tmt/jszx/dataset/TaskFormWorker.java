package com.tmt.jszx.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.WorkflowService;
import com.sg.bpm.workflow.runtime.Workflow;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class TaskFormWorker extends MasterDetailDataSetFactory {

	public TaskFormWorker() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				try {
					Object value = taskForm.getProcessInstanceVarible("dept",new CurrentAccountContext());

					System.out.println();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				Work work = taskForm.getWork();
//				List<Map<String, Object>> historys = (List<Map<String, Object>>) work
//						.getValue(IWorkCloneFields.F_WF_EXECUTE
//								+ IProcessControl.POSTFIX_HISTORY);
//
//				for (Map<String, Object> history : historys) {
//					String taskname = (String) history
//							.get(IProcessControl.F_WF_TASK_NAME);
//					if ("申请技术支持".equals(taskname)) {
//						Object dept = history.get("form_dept");
//						if (dept instanceof ObjectId) {
//							List<PrimaryObject> allUser = new ArrayList<PrimaryObject>();
//							Organization org = ModelService.createModelObject(
//									Organization.class, (ObjectId) dept);
//							return new DataSet(searchUser(allUser, org));
//						}
//					}
//
//				}
			}
		}
		return super.getDataSet();
	}

	private List<PrimaryObject> searchUser(List<PrimaryObject> list,
			Organization org) {
		list.addAll(org.getUser());
		List<PrimaryObject> childrenOrgs = org.getChildrenOrganization();
		if (childrenOrgs != null && childrenOrgs.size() > 0) {
			for (int i = 0; i < childrenOrgs.size(); i++) {
				Organization childrenOrg = (Organization) childrenOrgs.get(i);
				searchUser(list, childrenOrg);
			}
		}
		return list;
	}
}