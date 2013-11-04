package com.tmt.tb.field.parameter;

import com.sg.business.taskforms.AbstractRoleParameterDelegator;
import com.sg.business.taskforms.IRoleConstance;

public class DevelopmentDirector extends AbstractRoleParameterDelegator {


	@Override
	protected String getRoldNumber() {
		return IRoleConstance.ROLE_DEVELOPMENTDIRECTOR_ID;
	}

}
