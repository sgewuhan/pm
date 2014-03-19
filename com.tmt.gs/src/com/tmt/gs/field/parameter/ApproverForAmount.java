package com.tmt.gs.field.parameter;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.field.processparameter.AbstractRoleParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.taskforms.IRoleConstance;

public class ApproverForAmount extends AbstractRoleParameterDelegator {

	@Override
	protected Organization getOrganization(String processParameter,
			String taskDatakey, PrimaryObject taskFormData) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Work work = taskForm.getWork();
			if (work != null) {
				User charger = work.getCharger();
				if (charger != null) {
					return charger.getOrganization();
				}
			}
		}
		return null;
	}

	@Override
	protected Object setType(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		Object value = taskFormData.getValue(taskDatakey);
		return value;
	}

	@Override
	protected int getSelectType(Object type) {
		return Organization.ROLE_SEARCH_UP;
	}

	@Override
	protected String getRoldNumber(Object type) {
		String roleId = IRoleConstance.ROLE_MOLD_GENERAL_MANAGER_ID;
		if (type instanceof Double) {
			Double amount = (Double) type;
			if (amount < 10000d) {
				roleId = IRoleConstance.ROLE_MOLD_APPROVER_DEPT_ID;
			} else if (amount >= 10000d && amount < 100000d) {
				roleId = IRoleConstance.ROLE_MOLD_APPROVER_DEVICE_ID;
			} else if (amount >= 100000d && amount < 200000d) {
				roleId = IRoleConstance.ROLE_MOLD_MAIN_ENGINEER_ID;
			}
		}
		return roleId;
	}
}
