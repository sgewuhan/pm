package com.tmt.tb.validation;

import com.sg.business.commons.engineeringchange.AbstractChangeActivityValidator;

public class PackageChangeActivityValidator extends
		AbstractChangeActivityValidator {

	public PackageChangeActivityValidator() {
	}

	@Override
	public String getECNName() {
		return "包装变更"; //$NON-NLS-1$
	}

}
