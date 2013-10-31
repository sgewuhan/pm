package com.tmt.tb.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class UserSelectorOfTaskforms extends MasterDetailDataSetFactory {

	public UserSelectorOfTaskforms() {
		super(IModelConstants.DB, IModelConstants.C_USER);
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
				Work work = taskForm.getWork();
				List<PrimaryObject> result = new ArrayList<PrimaryObject>();
				List<Map<String, Object>> historys = (List<Map<String, Object>>) work
						.getValue(IWorkCloneFields.F_WF_EXECUTE
								+ IProcessControl.POSTFIX_HISTORY);
				for (Map<String, Object> history : historys) {
					String taskname = (String) history
							.get(IProcessControl.F_WF_TASK_NAME);
					if ("Åú×¼".equals(taskname)) {
						ObjectId form_dept = (ObjectId) history
								.get("form_dept");
						if (form_dept != null) {
							Organization org = ModelService.createModelObject(
									Organization.class, form_dept);
							result.add(org);
						}
					}
				}
				return new DataSet(result);
			}
		}
		return super.getDataSet();
	}

}
