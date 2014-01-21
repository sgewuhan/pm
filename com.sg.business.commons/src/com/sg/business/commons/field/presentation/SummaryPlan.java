package com.sg.business.commons.field.presentation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;
import com.sg.widgets.registry.config.FieldConfigurator;

public class SummaryPlan implements IValuePresentation {

	public SummaryPlan() {
	}

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();
		FieldConfigurator fc = field.getField();
		String format = fc.getFormat();
		SimpleDateFormat sdf = new SimpleDateFormat(
				Utils.isNullOrEmpty(format) ? Utils.SDF_DATE_COMPACT_SASH
						: format);

		PrimaryObject data = input.getData();
		StringBuffer text = new StringBuffer();//$NON-NLS-N$
		if (!(data instanceof Work)) {
			return "";
		}
		Work work = (Work) data;
		Date ps = work.getPlanStart();
		Date pf = work.getPlanFinish();
		if (ps != null) {
			text.append(sdf.format(ps));
		} else {
			text.append("       ");//$NON-NLS-N$
		}

		text.append(" ~ ");//$NON-NLS-N$

		if (ps != null) {
			text.append(sdf.format(pf));
		} else {
			text.append("       ");//$NON-NLS-N$
		}
		// 取工期
		Integer pd = work.getPlanDuration();
		text.append("  ");//$NON-NLS-N$
		text.append(Messages.get(field.getControl().getDisplay()).Duration);
		text.append(": ");//$NON-NLS-N$
		if(pd!=null){
			text.append(pd+"d");
		}else{
			text.append("?");
		}
		// 取工时
		Double pw = work.getPlanWorks();
		text.append("  ");//$NON-NLS-N$
		text.append(Messages.get(field.getControl().getDisplay()).Works);
		text.append(": ");//$NON-NLS-N$
		if(pw!=null){
			text.append(pw+"h");
		}else{
			text.append("?");
		}
		return text.toString();
	}

}
