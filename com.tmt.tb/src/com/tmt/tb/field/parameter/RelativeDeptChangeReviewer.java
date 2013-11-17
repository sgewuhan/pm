package com.tmt.tb.field.parameter;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;

public class RelativeDeptChangeReviewer implements IProcessParameterDelegator {

	public RelativeDeptChangeReviewer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if (value instanceof ObjectId) {
			Organization org = ModelService.createModelObject(Organization.class,
					(ObjectId) value);
			if(org!=null){
				List<String> users = org.getRoleAssignmentUserIds(
						IRoleConstance.ROLE_CHANGE_APPROVER_ID, 1);
				if (!users.isEmpty()) {
					return users.get(0);
				}
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}
}
