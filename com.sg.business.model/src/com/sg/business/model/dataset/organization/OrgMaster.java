package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class OrgMaster extends MasterDetailDataSetFactory {

	public OrgMaster() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Organization.F__ID;
	}

}
