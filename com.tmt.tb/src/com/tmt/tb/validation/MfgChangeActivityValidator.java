package com.tmt.tb.validation;

import com.sg.business.commons.engineeringchange.AbstractChangeActivityValidator;

public class MfgChangeActivityValidator extends AbstractChangeActivityValidator {

	public MfgChangeActivityValidator() {
	}

	@Override
	public String getECNName() {
		return "在制品处理"; //$NON-NLS-1$
	}

}
