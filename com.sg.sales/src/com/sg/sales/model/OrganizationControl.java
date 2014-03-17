package com.sg.sales.model;

import java.util.List;

import org.bson.types.BasicBSONList;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.sales.ISalesRole;

public class OrganizationControl extends PrimaryObject {

	
	public static String F_OWNER_ORGANIZATION_ID = "owner_org_id";
	
	@Override
	public void doInsert(IContext context) throws Exception {
		//设置默认的公司
		String userId = context.getAccountInfo().getConsignerId();
		User user = UserToolkit.getUserById(userId);
		Organization org = user.getOrganization();
		Organization company = org.getCompany();
		if(company!=null){
			Object value = getValue(F_OWNER_ORGANIZATION_ID);
			if(!(value instanceof BasicBSONList)){
				value = new BasicBSONList();
			}
			((BasicBSONList)value).add(company.get_id());
			setValue(F_OWNER_ORGANIZATION_ID,value );
		}
		super.doInsert(context);
	}

	public static DBObject getVisitableCondition(String userId) {
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> roles = user.getRoles(ISalesRole.CRM_ADMIN_NUMBER);
		if(roles!=null){
			BasicDBList orgIdSet = new BasicDBList();  
			for (int i = 0; i < roles.size(); i++) {
				Role role = (Role) roles.get(i);
				orgIdSet.add(new BasicDBObject().append(F_OWNER_ORGANIZATION_ID,role.getOrganization_id()));
			}
			if(orgIdSet.size()>0){
				return new BasicDBObject().append("$or", orgIdSet);
			}
		}
		return new BasicDBObject().append(F__ID, null);
	}
	
}
