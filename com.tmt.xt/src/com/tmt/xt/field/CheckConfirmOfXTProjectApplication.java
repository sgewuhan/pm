package com.tmt.xt.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckConfirmOfXTProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckConfirmOfXTProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
