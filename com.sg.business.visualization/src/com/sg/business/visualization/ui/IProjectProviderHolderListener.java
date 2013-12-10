package com.sg.business.visualization.ui;

import com.sg.business.model.IParameterListener;
import com.sg.business.model.ProjectProvider;

public interface IProjectProviderHolderListener extends IParameterListener {

	void projectProviderChanged(ProjectProvider newProjectProvider,
			ProjectProvider oldProjectProvider);

}
