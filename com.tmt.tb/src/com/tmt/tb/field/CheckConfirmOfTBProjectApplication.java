package com.tmt.tb.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckConfirmOfTBProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckConfirmOfTBProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·“È∆¿…Û".equals(choice)){
			return true;
		}
		return false;
	}

}
