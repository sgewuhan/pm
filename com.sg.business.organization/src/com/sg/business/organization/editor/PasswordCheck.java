package com.sg.business.organization.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class PasswordCheck extends AbstractValidator {

	public PasswordCheck() {
	}

	@Override
	protected String getValidMessage(PrimaryObject data) {
		Object password = data.getValue("password");
		Object password1 = data.getValue("password2");
		if(password==null||password.toString().isEmpty()){
			return "���벻��Ϊ��";
		}
		if(!password.equals(password1)){
			return "��������������벻��ͬ";
		}
		
		if(password.toString().length()<6){
			return "��������Ҫ����6λ����";
		}
		
		
		return null;
	}

}
