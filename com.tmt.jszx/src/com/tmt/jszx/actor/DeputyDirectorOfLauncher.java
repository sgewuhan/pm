package com.tmt.jszx.actor;

import com.sg.business.taskforms.AbstractRoleParameterDelegator;
import com.sg.business.taskforms.IRoleConstance;

public class DeputyDirectorOfLauncher extends AbstractRoleParameterDelegator {

	@Override
	protected String getRoldNumber() {
		return IRoleConstance.ROLE_DEPUTY_DIRECTOR_ID;
	}

}
