package com.sg.business.work.editor.field;

import com.mobnut.db.model.PrimaryObject;
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
				return "工作量完成百分比不能超过100%";
			}else{
				return null;
			}
		}
		return "类型不正确";
	}

}
