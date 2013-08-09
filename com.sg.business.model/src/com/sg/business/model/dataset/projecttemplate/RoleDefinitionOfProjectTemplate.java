package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class RoleDefinitionOfProjectTemplate extends MasterDetailDataSetFactory {

	public RoleDefinitionOfProjectTemplate() {
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
		setSort(new SEQSorter().getBSON());
	}

	@Override
	protected String getDetailCollectionKey() {
		return RoleDefinition.F_PROJECT_TEMPLATE_ID;
	}


}
