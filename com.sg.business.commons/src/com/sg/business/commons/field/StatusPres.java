package com.sg.business.commons.field;

import org.eclipse.ui.forms.IFormPart;

import com.sg.business.model.ModelUtil;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class StatusPres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		String value = (String) field.getValue();
		return ModelUtil.getLifecycleStatusText(value);
	}

}
