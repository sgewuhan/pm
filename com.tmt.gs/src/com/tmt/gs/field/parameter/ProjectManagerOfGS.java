package com.tmt.gs.field.parameter;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class ProjectManagerOfGS implements IProcessParameterDelegator {

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if (value instanceof ObjectId) {
			Project pro = ModelService.createModelObject(
					Project.class, (ObjectId) value);
			return pro.getChargerId();
			}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

}
