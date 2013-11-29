package com.tmt.tb.validation;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class HasPlanValidation extends AbstractValidator {

	public HasPlanValidation() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		if(Utils.isDenied((String) data.getValue("choice"))){
			return null;
		}
		if("是".equals(data.getValue("hasplan"))){
			if(data.getValue("plan")==null){
				return "请选择在制品审核者";
			}
		}
		return null;
		
	}

}
