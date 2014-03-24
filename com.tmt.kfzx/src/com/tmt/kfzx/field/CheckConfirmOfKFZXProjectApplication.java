package com.tmt.kfzx.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckConfirmOfKFZXProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckConfirmOfKFZXProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
