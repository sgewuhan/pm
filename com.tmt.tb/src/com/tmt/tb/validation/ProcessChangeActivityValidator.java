package com.tmt.tb.validation;

import com.sg.business.commons.engineeringchange.AbstractChangeActivityValidator;

public class ProcessChangeActivityValidator extends
		AbstractChangeActivityValidator {

	public ProcessChangeActivityValidator() {
	}

	@Override
	public String getECNName() {
		return "工艺变更"; //$NON-NLS-1$
	}

}
