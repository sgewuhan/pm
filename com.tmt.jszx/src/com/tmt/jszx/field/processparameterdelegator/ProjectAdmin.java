package com.tmt.jszx.field.processparameterdelegator;

import com.sg.business.model.Role;
import com.sg.business.taskforms.AbstractRoleParameterDelegator;

public class ProjectAdmin  extends AbstractRoleParameterDelegator {

	public ProjectAdmin() {
	}

	@Override
	protected String getRoldNumber() {
		return Role.ROLE_PROJECT_ADMIN_ID;
	}

}
