package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

public class OrganizationFinder extends OptionDataSetFactory {

	public OrganizationFinder() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}


	
}
