package com.tmt.gs.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class GSCheckReviewerOfTBProjectApplication extends
		AbstractCheckFieldByChoice {

	public GSCheckReviewerOfTBProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·«©∆¿…Û".equals(choice)||"ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
