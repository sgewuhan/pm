package com.sg.business.commons.field.processparameter;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public abstract class AbstractRoleParameterDelegator implements
		IProcessParameterDelegator {

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Organization org = getOrganization(processParameter, taskDatakey,
					taskFormData);
			Object type = setType(processParameter, taskDatakey, taskFormData);
			if (!org.isEmpty()) {
				List<String> users = org.getRoleAssignmentUserIds(
						getRoldNumber(type), getSelectType(type),
						taskForm.getWork());
				if (!users.isEmpty()) {
					return users.get(0);
				}
			}
		}
		return ((User) UserToolkit.getAdmin().get(0)).getUserid();
	}

	protected abstract Organization getOrganization(String processParameter,
			String taskDatakey, PrimaryObject taskFormData);

	protected abstract int getSelectType(Object type);

	protected abstract String getRoldNumber(Object type);

	protected abstract Object setType(String processParameter,
			String taskDatakey, PrimaryObject taskFormData);

}
