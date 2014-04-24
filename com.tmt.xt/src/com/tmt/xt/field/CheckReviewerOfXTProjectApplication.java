package com.tmt.xt.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfXTProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfXTProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·«©∆¿…Û".equals(choice)||"ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
