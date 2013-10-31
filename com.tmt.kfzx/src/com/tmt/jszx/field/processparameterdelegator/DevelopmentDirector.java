package com.tmt.jszx.field.processparameterdelegator;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public class DevelopmentDirector implements IProcessParameterDelegator {

	public DevelopmentDirector() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if(value instanceof ObjectId){
			Organization org = ModelService.createModelObject(Organization.class, (ObjectId)value);
			Role role = org.getRole(Role.ROLE_DEVELOPMENTDIRECTOR_ID, 1);
			if (role != null) {
				List<PrimaryObject> assignment = role.getAssignment();
				if (assignment != null && assignment.size() > 0) {
					return ((RoleAssignment) assignment.get(0)).getUserid();
				}
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

}