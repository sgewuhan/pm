package com.tmt.gs.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class GSCheckReviewerOfTBProjectApplication extends
		AbstractCheckFieldByChoice {

	public GSCheckReviewerOfTBProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("��ǩ����".equals(choice)||"��������".equals(choice)){ //$NON-NLS-1$
			return true;
		}
		return false;
	}

}
