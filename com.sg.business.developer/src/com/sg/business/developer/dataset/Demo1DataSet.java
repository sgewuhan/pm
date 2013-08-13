package com.sg.business.developer.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.developer.model.Demo1;
import com.sg.business.model.IModelConstants;

public class Demo1DataSet extends SingleDBCollectionDataSetFactory{

	public Demo1DataSet() {
		super(IModelConstants.DB, "demo1");
		setQueryCondition(new BasicDBObject().append(Demo1.F_PARENT_ID, null));
	}

}
