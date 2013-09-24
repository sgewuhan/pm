package com.sg.business.commons.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class SummaryPlanWorks implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();
		PrimaryObject data = input.getData();
		if (data instanceof Work) {
			Work work = (Work) data;
			Double value = work.getPlanWorks();
			if (value != null) {
				return value.toString();
			}

		}
		return "";
	}

}
