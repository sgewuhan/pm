package com.tmt.gs.field.parameter;

import java.util.List;


import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;

public class ApproverForAmount implements IProcessParameterDelegator {

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		// 1.获取金额
		Object value = taskFormData.getValue(taskDatakey);
		Double amount = (Double) value;
		// 2.获取组织：
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			Work work = taskForm.getWork();
			User charger = work.getCharger();
			Organization org = charger.getOrganization();
			// 3.获取角色id
			String roleId = IRoleConstance.ROLE_MOLD_GENERAL_MANAGER_ID;
			if (amount < 10000d) {
				roleId = IRoleConstance.ROLE_MOLD_APPROVER_DEPT_ID;
			} else if (amount >= 10000d && amount < 100000d) {
				roleId = IRoleConstance.ROLE_MOLD_APPROVER_DEVICE_ID;
			} else if (amount >= 100000d && amount < 200000d) {
				roleId = IRoleConstance.ROLE_MOLD_MAIN_ENGINEER_ID;
			}
			// 4.获取用户id：
			List<String> userIds = org.getRoleAssignmentUserIds(roleId,
					Organization.ROLE_SEARCH_UP);
			if (!userIds.isEmpty()) {
				return userIds.get(0);
			}
		}

		return UserToolkit.getAdmin().get(0).getValue(User.F_USER_ID);
	}
}
