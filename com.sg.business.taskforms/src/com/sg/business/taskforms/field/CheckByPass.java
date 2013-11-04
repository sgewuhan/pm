package com.sg.business.taskforms.field;

import com.mobnut.commons.util.Utils;

public class CheckByPass extends AbstractCheckFieldByChoice {

	@Override
	protected boolean choicePassValue(String choice) {
		return Utils.isAdmit(choice);
	}


}
