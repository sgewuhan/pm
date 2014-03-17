package com.sg.sales.model.dataset;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.sales.Sales;
import com.sg.sales.model.OrganizationControl;
import com.sg.widgets.part.CurrentAccountContext;

public class MntContractDataSet extends SingleDBCollectionDataSetFactory {

	public MntContractDataSet() {
		super(IModelConstants.DB, Sales.C_CONTRACT);
		String userid = new CurrentAccountContext().getConsignerId();
		setQueryCondition(OrganizationControl.getVisitableCondition(userid));
		setSort(new BasicDBObject().append(PrimaryObject.F__ID, -1));
	}

}
