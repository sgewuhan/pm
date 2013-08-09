package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class MemberofOrganization extends MasterDetailDataSetFactory {

	public MemberofOrganization() {
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}
}
