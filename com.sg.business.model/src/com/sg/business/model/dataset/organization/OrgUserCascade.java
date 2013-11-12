package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class OrgUserCascade extends MasterDetailDataSetFactory {

	public OrgUserCascade() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Organization.F__ID;
	}

	@Override
	protected Object getMasterValue() {
		
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		User user = UserToolkit.getUserById(userId);
		return user.getOrganization_id();
	}

}
