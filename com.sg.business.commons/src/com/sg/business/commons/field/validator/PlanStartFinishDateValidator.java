package com.sg.business.commons.field.validator;

import java.util.Date;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.nls.Messages;
import com.sg.business.model.Work;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class PlanStartFinishDateValidator extends AbstractValidator {

	@Override
	protected String getValidMessage(PrimaryObject data) {
		Object pStart = data.getValue(Work.F_PLAN_START);
		Object pFinish = data.getValue(Work.F_PLAN_FINISH);
		if(pStart instanceof Date){
			if(pFinish instanceof Date){
				return ((Date) pFinish).after((Date)pStart)?null:Messages.get().PlanStartFinishDateValidator_0;
			}
		}
		return null;
	}


}
