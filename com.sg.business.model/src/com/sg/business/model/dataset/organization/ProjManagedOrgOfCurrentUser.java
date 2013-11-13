package com.sg.business.model.dataset.organization;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjManagedOrgOfCurrentUser extends SingleDBCollectionDataSetFactory {

	private User user;

	public ProjManagedOrgOfCurrentUser() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public DataSet getDataSet() {
		
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_PROJECT_ADMIN_ID);
		
		return new DataSet(orglist);
	}
}
