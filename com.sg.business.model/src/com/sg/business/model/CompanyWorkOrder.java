package com.sg.business.model;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class CompanyWorkOrder extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organizationid"; //$NON-NLS-1$
	
	public static final String F_WORKORDER = "workorderid"; //$NON-NLS-1$

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] getWorkOrders() {
//		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
//				IModelConstants.C_COMPANY_WORKORDER);
//		DBCursor cur = col.find();
//		String[] result = new String[cur.size()];
//		int i=0;
//		while(cur.hasNext()){
//			DBObject dbo = cur.next();
//			result[i++] = (String) dbo.get(F_WORKORDER);
//		}
		
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,IModelConstants.C_PROJECT);
		List res = col.distinct(Project.F_WORK_ORDER);
		return (String[]) res.toArray(new String[0]);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] getWorkOrders(Organization org) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,IModelConstants.C_PROJECT);
		List<ObjectId> childrenFunctionOrgId = org.getChildrenFunctionOrgId();
		BasicDBObject query = new BasicDBObject();
		query.put(Project.F_FUNCTION_ORGANIZATION, new BasicDBObject().append("$in", childrenFunctionOrgId));		
		List res = col.distinct(Project.F_WORK_ORDER,query);
		return (String[]) res.toArray(new String[0]);
	}

}
