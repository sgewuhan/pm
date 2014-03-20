package com.sg.business.taskforms.actor;

import com.sg.business.commons.actor.AbstractActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;

public class SupervisorOfLauncher extends AbstractActorIdProvider {

	@Override
	protected int getSelectType() {
		return Organization.ROLE_NOT_SEARCH;
	}

	@Override
	protected String getRoleNumber() {
		return Role.ROLE_DEPT_MANAGER_ID;
	}


}
