package com.sg.business.visualization.data;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class VisNavigatorDataSetForSales extends VisNavigatorDataSet {

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		OrganizationProjectSetFolderForSales orgFolder = ModelService
				.createModelObject(OrganizationProjectSetFolderForSales.class);
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
}
