package com.tmt.xt.field.parameter;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.field.processparameter.AbstractRoleParameterDelegator;
import com.sg.business.model.Organization;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.part.CurrentAccountContext;

public class SupportNotice extends AbstractRoleParameterDelegator {

	@Override
	protected Organization getOrganization(String processParameter,
			String taskDatakey, PrimaryObject taskFormData) {
		if (taskFormData instanceof TaskForm) {
			TaskForm taskForm = (TaskForm) taskFormData;
			try {
				String act_approve = (String) taskForm
						.getProcessInstanceVarible("act_approve", //$NON-NLS-1$
								new CurrentAccountContext());
				User user = UserToolkit.getUserById(act_approve);
				if (user != null) {
					return user.getOrganization();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected int getSelectType(Object type) {
		return Organization.ROLE_SEARCH_UP;
	}

	@Override
	protected String getRoldNumber(Object type) {
		return IRoleConstance.ROLE_SUPPORT_NOTICE_ID;
	}

	@Override
	protected Object setType(String processParameter, String taskDatakey,
			PrimaryObject taskFormData) {
		return null;
	}
}
