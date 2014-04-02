package com.tmt.gs.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckConfirmOfGSProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckConfirmOfGSProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
