package com.sg.business.taskforms.field;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public abstract class AbstractCheckFieldByChoice extends AbstractValidator {

	public AbstractCheckFieldByChoice() {
	}


	@Override
	protected String getValidMessage(PrimaryObject data) {
		String choice = data.getStringValue("choice");
		if(choicePassValue(choice)){
			String name = getFieldConfigurator().getLabel();
			return "«Î»∑∂®\""+name+"\"";
		}
		return null;
	}


	protected abstract boolean choicePassValue(String choice);

}
