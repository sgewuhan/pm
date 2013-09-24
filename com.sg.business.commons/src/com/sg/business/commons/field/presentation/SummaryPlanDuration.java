package com.sg.business.commons.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class SummaryPlanDuration implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();
		PrimaryObject data = input.getData();
		if (data instanceof Work) {
			Work work = (Work) data;
			try {
				work.checkAndCalculateDuration(Work.F_PLAN_START, Work.F_PLAN_FINISH, Work.F_PLAN_DURATION);
				Integer value = work.getPlanDuration();
				if (value != null) {
					return value.toString();
				}
			} catch (Exception e) {
			}

		}
		return "";
	}

}
