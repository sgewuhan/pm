package com.sg.business.model.dataset.work;


import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class WorkDeliverable  extends MasterDetailDataSetFactory{

	public WorkDeliverable(String dbName, String collectionName) {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	protected String getDetailCollectionKey() {
		return (String)getMasterValue();
	}

}
