package com.sg.business.developer.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;

public class TestDataSet extends SingleDBCollectionDataSetFactory {

	public TestDataSet() {
		super("pm2","test");
	}

}
