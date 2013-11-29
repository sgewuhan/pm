package com.tmt.tb.validation;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class RelatedDeptValidation extends AbstractValidator {

	public RelatedDeptValidation() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		if(Utils.isDenied((String) data.getValue("choice"))){
			return null;
		}
		if("是".equals(data.getValue("hasother"))){
			if(data.getValue("other")==null){
				return "请选择变更关联的其他部门";
			}
		}
		return null;
		
	}

}
