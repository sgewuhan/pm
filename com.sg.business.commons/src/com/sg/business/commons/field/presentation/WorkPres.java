package com.sg.business.commons.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class WorkPres implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();

		PrimaryObject data = input.getData();
		if (!(data instanceof Work)) {
			return "";
		}
		Work work = (Work) data;
		StringBuffer sb = new StringBuffer();
		String workDesc = work.getDesc();
		sb.append(workDesc);
		if(work.isSummaryWork()){
			sb.append(" [总成型工作]");
		}
		sb.append(" <q style='color:rgb(0,128,0);'>");
		sb.append(work.getLifecycleStatusText());
		sb.append("</q> ");
		if (work.isMilestone()) {
			String imageUrl = "<img src='" + FileUtil.getImageURL(BusinessResource.IMAGE_FLAG_RED_16, BusinessResource.PLUGIN_ID) //$NON-NLS-1$
					+ "' width='12' height='12' />"; //$NON-NLS-1$
			sb.append(imageUrl);
		}
		return sb.toString();
	}

}
