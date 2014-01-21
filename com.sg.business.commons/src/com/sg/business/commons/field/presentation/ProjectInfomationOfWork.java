package com.sg.business.commons.field.presentation;

import org.eclipse.ui.forms.IFormPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.valuepresentation.IValuePresentation;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.fields.AbstractFieldPart;

public class ProjectInfomationOfWork implements IValuePresentation {

	@Override
	public String getPresentValue(IFormPart part) {
		AbstractFieldPart field = (AbstractFieldPart) part;
		PrimaryObjectEditorInput input = field.getInput();
		PrimaryObject data = input.getData();
		if(data instanceof IProjectRelative){
			Project project = ((IProjectRelative)data).getProject();
			if(project!=null){
				return project.getLabel();				
			}
		}

		return Messages.get().ProjectInfomationOfWork_0;
	}

}
