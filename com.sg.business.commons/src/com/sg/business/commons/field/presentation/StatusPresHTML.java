package com.sg.business.commons.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.sg.business.model.toolkit.LifecycleToolkit;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class StatusPresHTML implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		String value = (String) field.getValue();
		String status = LifecycleToolkit.getLifecycleStatusText(value);

		return "<span style='color=#4a4a4a'>"+status+"</span>"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
