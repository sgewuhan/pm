package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.IProjectRelative;
import com.sg.business.model.Project;

public class ProjectColumnLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof IProjectRelative){
			Project project = ((IProjectRelative) element).getProject();
			if(project!=null){
				return project.getLabel();
			}
		}
		return super.getText(element);
	}
}
