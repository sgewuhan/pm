package com.tmt.jszx.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class TaskFormsReviewer extends MasterDetailDataSetFactory {

	public TaskFormsReviewer() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataSet getDataSet() {
		if (master != null) {
			if (master instanceof TaskForm) {
				TaskForm taskForm = (TaskForm) master;
				Object obj;
				try {
					obj = taskForm.getProcessInstanceVarible("reviewer_list",
							new CurrentAccountContext());
					List<PrimaryObject> result = new ArrayList<PrimaryObject>();
					if (obj instanceof ArrayList) {
						List<String> reviewer_list = (ArrayList<String>) obj;
						if (reviewer_list != null) {
							for (int i = 0; i < reviewer_list.size(); i++) {
								String userid = (String) reviewer_list.get(i);
								User user = UserToolkit.getUserById(userid);
								result.add(user);
							}
						}
					}
					return new DataSet(result);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.getDataSet();
	}

	// Work work = taskForm.getWork();
	// List<PrimaryObject> result = new ArrayList<PrimaryObject>();
	// List<Map<String, Object>> historys = (List<Map<String, Object>>) work
	// .getValue(IWorkCloneFields.F_WF_EXECUTE
	// + IProcessControl.POSTFIX_HISTORY);
	// for (Map<String, Object> history : historys) {
	// String taskname = (String) history.get(IProcessControl.F_WF_TASK_NAME);
	// if("Ã·Ωª∆¿…Û".equals(taskname)){
	// List<?> form_reviewer_list = (List<?>) history.get("form_reviewer_list");
	// if (form_reviewer_list != null) {
	// for (int i = 0; i < form_reviewer_list.size(); i++) {
	// String userid = (String) form_reviewer_list.get(i);
	// User user = UserToolkit.getUserById(userid);
	// result.add(user);
	// }
	// }
	// }
	// }

}
