package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class RoleDefinitionOfMasterProjectTemplate extends MasterDetailDataSetFactory {

	public RoleDefinitionOfMasterProjectTemplate() {
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return RoleDefinition.F_PROJECT_TEMPLATE_ID;
	}

}
