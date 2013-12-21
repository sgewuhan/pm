package com.tmt.tb.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfTBProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfTBProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·«©∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
