package com.sg.business.visualization.dataset;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class VisNavigatorDataSet extends DataSetFactory {

	private User user;

	public VisNavigatorDataSet() {
		String userId = new CurrentAccountContext().getAccountInfo()
				.getUserId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		OrganizationProjectSetFolder orgFolder = ModelService
				.createModelObject(OrganizationProjectSetFolder.class);
		orgFolder.setUser(user);
		TypeProjectSetFolder productFolder = ModelService
				.createModelObject(TypeProjectSetFolder.class);
		productFolder.setUser(user);
		OwnerProjectSetFolder ownerFolder = ModelService
				.createModelObject(OwnerProjectSetFolder.class);
		ownerFolder.setUser(user);
		result.add(orgFolder);
		result.add(productFolder);
		result.add(ownerFolder);
		return result;
	}

	@Override
	public long getTotalCount() {
		return 3;
	}

}
