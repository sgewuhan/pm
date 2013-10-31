package com.tmt.jszx.field.parameter;

import com.sg.business.taskforms.AbstractRoleParameterDelegator;
import com.sg.business.taskforms.IRoleConstance;

public class DeputyDirector extends AbstractRoleParameterDelegator {

	@Override
	protected String getRoldNumber() {
		return IRoleConstance.ROLE_DEPUTY_DIRECTOR_ID;
	}


}
