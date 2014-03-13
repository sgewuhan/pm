package com.sg.sales.model.dataset;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.Contact;

public class MyContactDataSet extends SingleDBCollectionDataSetFactory {

	public MyContactDataSet() {
		super(IModelConstants.DB, Sales.C_CONTACT);
		setSort(new BasicDBObject().append(Contact.F_DESC, -1));
	}

}
