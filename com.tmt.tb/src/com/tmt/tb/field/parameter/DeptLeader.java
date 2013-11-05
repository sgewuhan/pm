package com.tmt.tb.field.parameter;

import com.sg.business.model.Role;
import com.sg.business.taskforms.AbstractRoleParameterDelegator;

public class DeptLeader extends AbstractRoleParameterDelegator {

	@Override
	protected String getRoldNumber() {
		return Role.ROLE_DEPT_MANAGER_ID;
	}

}
