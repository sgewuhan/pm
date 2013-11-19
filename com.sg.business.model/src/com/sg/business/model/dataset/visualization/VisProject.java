package com.sg.business.model.dataset.visualization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class VisProject extends MasterDetailDataSetFactory {

	public VisProject() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Project.F_FUNCTION_ORGANIZATION;
	}

}
