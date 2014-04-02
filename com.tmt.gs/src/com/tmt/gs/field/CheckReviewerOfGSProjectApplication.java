package com.tmt.gs.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfGSProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfGSProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·«©∆¿…Û".equals(choice)||"ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
