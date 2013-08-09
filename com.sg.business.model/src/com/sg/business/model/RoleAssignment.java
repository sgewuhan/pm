package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class RoleAssignment extends PrimaryObject{

	public static final String F_USER_ID = "userid";
	
	public static final String F_USER_NAME = "username";
	
	public static final String F_ROLE_NAME = "rolename";

	public static final String F_ROLE_NUMBER = "rolenumber";
	
	public static final String F_ROLE_ID = "role_id";

	public String getUsername() {
		return (String) getValue(F_USER_NAME);
	}
	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}
	@Override
	public boolean canEdit(IContext context) {
		return false;
	}
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_USER_16);
	}
	
}
