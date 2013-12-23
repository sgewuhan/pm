package com.sg.business.performence.works;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.performence.nls.Messages;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class WorksFinishedPercentValidator extends AbstractValidator {


	@Override
	protected String getValidMessage(PrimaryObject data) {
		Object value = getValueForUpdate();
		if(value == null){
			return null;
		}
		if(value instanceof Number){
			Number number = (Number) value;
			if(number.doubleValue()>1){
				return Messages.get().WorksFinishedPercentValidator_0;
			}else{
				return null;
			}
		}
		return Messages.get().WorksFinishedPercentValidator_1;
	}

}
