package com.tmt.tb.field;

import com.sg.business.taskforms.field.AbstractCheckFieldByChoice;

public class CheckReviewerOfTBProjectApplication extends
		AbstractCheckFieldByChoice {

	public CheckReviewerOfTBProjectApplication() {
	}

	@Override
	protected boolean choicePassValue(String choice) {
		if("��ǩ����".equals(choice)){
			return true;
		}
		return false;
	}

}
