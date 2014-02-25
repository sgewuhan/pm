package com.sg.business.taskforms.field;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public abstract class AbstractCheckFieldByChoice extends AbstractValidator {

	public AbstractCheckFieldByChoice() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		String choice = data.getStringValue("choice"); //$NON-NLS-1$
		if (choicePassValue(choice)) {
			String label = getFieldConfigurator().getLabel();
			String name = getFieldConfigurator().getName();
			Object value = data.getValue(name);
			if (value == null) {
				return "��ȷ��" + label + "\""; 
			}
		}
		return null;
	}

	protected abstract boolean choicePassValue(String choice);

}
