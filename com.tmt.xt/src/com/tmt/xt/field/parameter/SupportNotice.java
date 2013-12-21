package com.tmt.xt.field.parameter;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.sg.bpm.workflow.taskform.IProcessParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.part.CurrentAccountContext;

public class SupportNotice implements IProcessParameterDelegator {

	public SupportNotice() {
	}

	@Override
	public Object getValue(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				String act_approve = (String) taskForm
						.getProcessInstanceVarible("act_approve", //$NON-NLS-1$
								new CurrentAccountContext());
				User user = UserToolkit.getUserById(act_approve);
				if (user != null) {
					Organization org = user.getOrganization();
					if (org != null) {
						List<String> userIds = org.getRoleAssignmentUserIds(
								IRoleConstance.ROLE_SUPPORT_NOTICE_ID,
								Organization.ROLE_SEARCH_UP);
						return userIds;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
