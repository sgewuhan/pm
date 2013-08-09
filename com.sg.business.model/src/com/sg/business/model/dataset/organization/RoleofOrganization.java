package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class RoleofOrganization extends MasterDetailDataSetFactory {

	public RoleofOrganization() {
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
