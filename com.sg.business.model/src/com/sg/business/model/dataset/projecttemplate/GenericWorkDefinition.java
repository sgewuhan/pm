package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class GenericWorkDefinition extends MasterDetailDataSetFactory {

	public GenericWorkDefinition() {
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F__ID;
	}

}
