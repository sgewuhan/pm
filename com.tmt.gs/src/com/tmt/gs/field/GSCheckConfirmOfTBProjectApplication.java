package com.tmt.gs.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class GSCheckConfirmOfTBProjectApplication extends
		AbstractCheckFieldByChoice {

	public GSCheckConfirmOfTBProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("��������".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
