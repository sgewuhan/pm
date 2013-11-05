package com.sg.business.commons.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class VersionPres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObject data = field.getInput().getData();
		if(data instanceof Document){
			return ((Document)data) .getRevId();
		}
		return "";
	}

}
