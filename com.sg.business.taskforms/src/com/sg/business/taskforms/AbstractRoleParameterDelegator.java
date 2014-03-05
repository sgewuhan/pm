package com.sg.business.taskforms;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public abstract class AbstractRoleParameterDelegator implements IProcessParameterDelegator {


	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if (value instanceof ObjectId) {
			Organization org = ModelService.createModelObject(
					Organization.class, (ObjectId) value);
			List<String> users = org.getRoleAssignmentUserIds(
					getRoldNumber(), Organization.ROLE_SEARCH_UP);
			if(!users.isEmpty()){
				return users.get(0);
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

	protected abstract String getRoldNumber() ;

}
