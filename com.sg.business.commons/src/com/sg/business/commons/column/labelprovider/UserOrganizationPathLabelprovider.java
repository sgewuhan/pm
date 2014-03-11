package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Organization;
import com.sg.business.model.User;

public class UserOrganizationPathLabelprovider extends ColumnLabelProvider {

//	private DBCollection orgCol;
//
//	public UserOrganizationPathLabelprovider() {
//		super();
//		orgCol = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
//	}

	@Override
	public String getText(Object element) {
		User user = (User) element;
		Organization org = user.getOrganization();
		if(org == null){
			return "";
		}
		return org.getPath(2);
//		ObjectId parentId = (ObjectId) dbo.getValue(User.F_ORGANIZATION_ID);
//		if(parentId==null){
//			return ""; //$NON-NLS-1$
//		}
//		return getPath(parentId);
	}

//	private String getPath(ObjectId id) {
//		DBObject org=orgCol.findOne(new BasicDBObject().append(Organization.F__ID,id));
//		if(org == null){
//			return ""; //$NON-NLS-1$
//		}
//		String fullDesc = (String) org.get(Organization.F_FULLDESC);
//		return fullDesc==null?"":fullDesc; //$NON-NLS-1$
////		DBObject org = orgCol.findOne(new BasicDBObject().append(Organization.F__ID,id));
////		ObjectId parentId = (ObjectId) org.get(Organization.F_PARENT_ID);
////		String orgName = (String) org.get(Organization.F_DESC);
////		if(parentId!=null){
////			return getPath(parentId)+">"+orgName;
////		}else{
////			return orgName;
////		}
//	}


}
