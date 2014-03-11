package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;

public class UserRoleAssignmentLabelprovider extends ColumnLabelProvider {

	private DBCollection raCol;

	public UserRoleAssignmentLabelprovider() {
		super();
		raCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_ASSIGNMENT);
	}

	@Override
	public String getText(Object element) {
		User user = (User)element;
		DBCursor cur = raCol.find(new BasicDBObject().append(
				RoleAssignment.F_USER_ID, user.getUserid()));
		String rolename = null;
		while (cur.hasNext()) {
			DBObject ra = cur.next();
			if (rolename != null) {
				rolename += ", "+ra.get(RoleAssignment.F_ROLE_NAME); //$NON-NLS-1$
			}else{
				rolename = ""+ra.get(RoleAssignment.F_ROLE_NAME); //$NON-NLS-1$
			}
		}
		return rolename==null?"":rolename; //$NON-NLS-1$
	}

}
