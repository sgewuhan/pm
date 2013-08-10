package com.sg.business.model.dataset.demo;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class DemoDataSet extends SingleDBCollectionDataSetFactory {

	public DemoDataSet() {
		super(IModelConstants.DB, IModelConstants.C_DEMO);
	}
}
