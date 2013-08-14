package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorkDefConnectionOfProjectTemplate extends
		MasterDetailDataSetFactory {

	public WorkDefConnectionOfProjectTemplate() {
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION_CONNECTION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID;
	}

}
