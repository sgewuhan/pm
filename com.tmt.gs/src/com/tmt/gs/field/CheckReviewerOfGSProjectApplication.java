package com.tmt.gs.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfGSProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfGSProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("��ǩ����".equals(choice)||"��������".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
