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
			return "密码不可为空";
		}
		if(!password.equals(password1)){
			return "您两次输入的密码不相同";
		}
		
		if(password.toString().length()<6){
			return "您至少需要输入6位密码";
		}
		
		
		return null;
	}

}
