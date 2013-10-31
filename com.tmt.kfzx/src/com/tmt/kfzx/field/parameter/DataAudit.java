package com.tmt.kfzx.field.parameter;

import com.sg.business.taskforms.AbstractRoleParameterDelegator;
import com.sg.business.taskforms.IRoleConstance;

public class DataAudit extends AbstractRoleParameterDelegator {



	@Override
	protected String getRoldNumber() {
		return IRoleConstance.ROLE_DATAAUDIT_ID;
	}

}
