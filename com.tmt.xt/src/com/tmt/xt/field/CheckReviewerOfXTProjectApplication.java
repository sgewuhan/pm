package com.tmt.xt.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfXTProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfXTProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("��ǩ����".equals(choice)||"��������".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
