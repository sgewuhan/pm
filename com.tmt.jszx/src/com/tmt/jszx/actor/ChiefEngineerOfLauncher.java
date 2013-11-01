package com.tmt.jszx.actor;

import com.sg.business.taskforms.AbstractRoleParameterDelegator;
import com.sg.business.taskforms.IRoleConstance;

public class ChiefEngineerOfLauncher extends AbstractRoleParameterDelegator {

	@Override
	protected String getRoldNumber() {
		return IRoleConstance.ROLE_CHIEF_ENGINEER_ID;
	}

}
