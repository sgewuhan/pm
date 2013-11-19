package com.sg.business.model.dataset;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class ProjectProduct extends SingleDBCollectionDataSetFactory {

	
	public ProjectProduct() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	
	@Override
	public DataSet getDataSet() {
		// TODO Auto-generated method stub
		return super.getDataSet();
	}

}
