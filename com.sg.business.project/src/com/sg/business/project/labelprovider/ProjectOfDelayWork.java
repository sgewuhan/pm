package com.sg.business.project.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class ProjectOfDelayWork extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Work) {
			Work work = (Work) element;
			Project project = work.getProject();
			if (project != null) {
				return project.getDesc();
			}
		}
		return ""; //$NON-NLS-1$
	}
}
