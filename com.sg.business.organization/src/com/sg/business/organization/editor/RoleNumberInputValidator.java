package com.sg.business.organization.editor;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Role;
import com.sg.widgets.commons.valuevalidator.AbstractValidator;

public class RoleNumberInputValidator extends AbstractValidator {

	@Override
	protected String getValidMessage(PrimaryObject data) {
		Role role = (Role)data;
		try {
			role.check();
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}


}
