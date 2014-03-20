package com.sg.business.taskforms.actor;

import com.sg.business.commons.actor.AbstractActorIdProvider;
import com.sg.business.model.Organization;
import com.sg.business.taskforms.IRoleConstance;

public class DeputyDirectorOfLauncher extends AbstractActorIdProvider {

	@Override
	protected int getSelectType() {
		return Organization.ROLE_SEARCH_UP;
	}

	@Override
	protected String getRoleNumber() {
		return IRoleConstance.ROLE_DEPUTY_DIRECTOR_ID;
	}


}
