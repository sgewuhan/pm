package com.tmt.tb.field.parameter;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;

public class SupDeptChiefengineer implements IProcessParameterDelegator {

	public SupDeptChiefengineer() {
	}

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		if (value instanceof ObjectId) {
			Project pro = ModelService.createModelObject(Project.class,
					(ObjectId) value);
			Organization functionOrg = pro.getFunctionOrganization();
			Organization parentOrg = (Organization) functionOrg
					.getParentOrganization();
			if(parentOrg!=null){
				List<String> users = parentOrg.getRoleAssignmentUserIds(
						IRoleConstance.ROLE_CHANGE_APPROVER_ID, 1);
				if (!users.isEmpty()) {
					return users.get(0);
				}
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

}
