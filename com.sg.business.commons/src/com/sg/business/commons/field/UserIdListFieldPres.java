package com.sg.business.commons.field;

import org.eclipse.ui.forms.IFormPart;

import com.sg.business.model.Message;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class UserIdListFieldPres implements IValuePresentation {

	public UserIdListFieldPres() {
	}

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		Message message = (Message) field.getInput().getData();
		return message.getRecieverLabel();
	}

}
