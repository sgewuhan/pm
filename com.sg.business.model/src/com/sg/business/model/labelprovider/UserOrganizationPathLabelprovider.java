package com.sg.business.model.labelprovider;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;

public class UserOrganizationPathLabelprovider extends ColumnLabelProvider {

	private DBCollection orgCol;

	public UserOrganizationPathLabelprovider() {
		super();
		orgCol = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	@Override
	public String getText(Object element) {
		PrimaryObject dbo = ((PrimaryObject)element);
		ObjectId parentId = (ObjectId) dbo.getValue(User.F_ORGANIZATION_ID);
		if(parentId==null){
			return "";
		}
		return getPath(parentId);
	}

	private String getPath(ObjectId id) {
		DBObject org = orgCol.findOne(new BasicDBObject().append(Organization.F__ID,id));
		if(org == null){
			return "";
		}
		ObjectId parentId = (ObjectId) org.get(Organization.F_PARENT_ID);
		String orgName = (String) org.get(Organization.F_DESC);
		if(parentId!=null){
			return getPath(parentId)+">"+orgName;
		}else{
			return orgName;
		}
	}


}
