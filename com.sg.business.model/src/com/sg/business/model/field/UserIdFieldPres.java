package com.sg.business.model.field;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.User;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class UserIdFieldPres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		String value = (String) field.getValue();
		if(!Utils.isNullOrEmpty(value)){
			User user = User.getUserById(value);
			if(user!=null){
				return user.getLabel();
			}
		}
		return "";
	}

}
