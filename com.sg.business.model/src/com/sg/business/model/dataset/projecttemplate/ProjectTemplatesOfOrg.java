package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectTemplatesOfOrg extends MasterDetailDataSetFactory {

	public ProjectTemplatesOfOrg() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F_ORGANIZATION_ID;
	}
	
	
}
