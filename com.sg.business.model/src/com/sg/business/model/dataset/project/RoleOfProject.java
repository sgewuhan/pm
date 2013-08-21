package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectRole;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class RoleOfProject extends MasterDetailDataSetFactory {

	public RoleOfProject() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_ROLE);
	}

	@Override
	protected String getDetailCollectionKey() {
		return ProjectRole.F_PROJECT_ID;
	}

}
