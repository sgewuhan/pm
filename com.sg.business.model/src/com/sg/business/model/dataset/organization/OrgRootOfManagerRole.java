package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class OrgRootOfManagerRole extends MasterDetailDataSetFactory {

	private User user;

	public OrgRootOfManagerRole() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		String userId = new CurrentAccountContext().getUserId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> orglist = new ArrayList<PrimaryObject>();
		if (master == null) {
			orglist = user
					.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);
		} else if (master instanceof Organization) {
			orglist.add(master);
		}
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			boolean hasParent = false;
			for (int j = 0; j < input.size(); j++) {
				Organization inputOrg = (Organization) input.get(j);
				if (inputOrg.isSuperOf(org)) {
					hasParent = true;
					break;
				}
				if (org.isSuperOf(inputOrg)) {
					input.remove(j);
					break;
				}
			}
			if (!hasParent) {
				input.add(org);
			}
		}
		return new DataSet(orglist);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

}
