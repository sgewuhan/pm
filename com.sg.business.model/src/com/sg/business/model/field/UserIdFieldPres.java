package com.sg.business.model.field;

import org.eclipse.ui.forms.IFormPart;

import com.sg.business.model.User;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class UserIdFieldPres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		String value = (String) field.getValue();
		if(value != null){
			User user = User.getUserById(value);
			return user.getLabel();
		}else{
			return "";
		}
	}

}
