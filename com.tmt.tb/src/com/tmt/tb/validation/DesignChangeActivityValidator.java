package com.tmt.tb.validation;

import com.sg.business.commons.engineeringchange.AbstractChangeActivityValidator;

public class DesignChangeActivityValidator extends
		AbstractChangeActivityValidator {

	public DesignChangeActivityValidator() {
	}

	@Override
	public String getECNName() {
		return "设计变更"; //$NON-NLS-1$
	}

}
