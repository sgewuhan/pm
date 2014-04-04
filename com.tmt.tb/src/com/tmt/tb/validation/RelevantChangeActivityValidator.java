package com.tmt.tb.validation;

import com.sg.business.commons.engineeringchange.AbstractChangeActivityValidator;

public class RelevantChangeActivityValidator extends
		AbstractChangeActivityValidator {

	public RelevantChangeActivityValidator() {
	}

	@Override
	public String getECNName() {
		return "相关部门变更"; //$NON-NLS-1$
	}

}
