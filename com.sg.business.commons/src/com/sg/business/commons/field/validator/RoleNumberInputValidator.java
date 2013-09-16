package com.sg.business.commons.field.validator;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Role;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class RoleNumberInputValidator extends AbstractValidator {

	@Override
	protected String getValidMessage(PrimaryObject data) {
		if(data instanceof Role){
			Role role = (Role)data;
			try {
				role.check();
				return null;
			} catch (Exception e) {
				return e.getMessage();
			}
		}else if(data instanceof AbstractRoleDefinition){
			AbstractRoleDefinition abstractRoleDefinition = (AbstractRoleDefinition) data;
			try {
				abstractRoleDefinition.check();
				return null;
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return null;
	}


}
