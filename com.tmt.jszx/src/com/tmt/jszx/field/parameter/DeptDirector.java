package com.tmt.jszx.field.parameter;

import com.sg.business.model.Role;
import com.sg.business.taskforms.AbstractRoleParameterDelegator;

public class DeptDirector extends AbstractRoleParameterDelegator {


	@Override
	protected String getRoldNumber() {
		return Role.ROLE_DEPT_MANAGER_ID;
	}

}
