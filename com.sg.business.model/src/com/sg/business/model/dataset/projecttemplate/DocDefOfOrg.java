package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class DocDefOfOrg extends
		MasterDetailDataSetFactory {

	public DocDefOfOrg() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT_DEFINITION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return DocumentDefinition.F_ORGANIZATION_ID;
	}

}
