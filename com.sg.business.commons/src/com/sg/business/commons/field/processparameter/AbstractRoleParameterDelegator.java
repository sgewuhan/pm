package com.sg.business.commons.field.processparameter;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public abstract class AbstractRoleParameterDelegator implements
		IProcessParameterDelegator {

	private Object type;

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Organization org = getOrganization(processParameter, taskDatakey,
				taskFormData);
		if (!org.isEmpty()) {
			List<String> users = org.getRoleAssignmentUserIds(getRoldNumber(type),
					getSelectType(type));
			if (!users.isEmpty()) {
				return users.get(0);
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

	protected abstract Organization getOrganization(String processParameter,
			String taskDatakey, PrimaryObject taskFormData);

	protected abstract int getSelectType(Object type);

	protected abstract String getRoldNumber(Object type);

	protected void setType(Object type) {
		this.type = type;
	}

}
