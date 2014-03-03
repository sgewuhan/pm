package com.sg.sales.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.sales.model.ISalesWork;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class ActionTypePres implements IValuePresentation {


	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();

		PrimaryObject data = input.getData();
		if (!(data instanceof Work)) {
			return "";
		}
		Work work = (Work) data;
		return work.getText(ISalesWork.F_ACTIONTYPE);
	}

}
