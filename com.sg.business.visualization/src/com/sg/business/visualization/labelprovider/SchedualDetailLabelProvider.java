package com.sg.business.visualization.labelprovider;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class SchedualDetailLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		
		ProjectPresentation pres = project.getPresentation();
		return pres.getSchedualDetailHTMLLabel();
		
	}


}