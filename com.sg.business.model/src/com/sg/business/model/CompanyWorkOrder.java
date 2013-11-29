package com.sg.business.model;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class CompanyWorkOrder extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organizationid";
	
	public static final String F_WORKORDER = "workorderid";

	public static String[] getWorkOrders() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_COMPANY_WORKORDER);
		DBCursor cur = col.find();
		String[] result = new String[cur.size()];
		int i=0;
		while(cur.hasNext()){
			DBObject dbo = cur.next();
			result[i++] = (String) dbo.get(F_WORKORDER);
		}
		return result;
	}
	

}
