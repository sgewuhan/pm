package com.tmt.kfzx.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfKFZXProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfKFZXProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("��ǩ����".equals(choice)||"��������".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
