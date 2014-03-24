package com.tmt.kfzx.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfKFZXProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfKFZXProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("ª·«©∆¿…Û".equals(choice)||"ª·“È∆¿…Û".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
