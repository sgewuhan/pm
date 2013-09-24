package com.sg.business.commons.field.presentation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.registry.config.FieldConfigurator;

public class SummaryPlanFinish implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();
		FieldConfigurator fc = field.getField();
		String format = fc.getFormat();
		PrimaryObject data = input.getData();
		if (data instanceof Work) {
			Work work = (Work) data;
			Date date = work.getPlanFinish();
			if (date != null) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						Utils.isNullOrEmpty(format) ? Utils.SDF_DATE_COMPACT_SASH
								: format);
				return sdf.format(date);
			}

		}
		return "";
	}

}
