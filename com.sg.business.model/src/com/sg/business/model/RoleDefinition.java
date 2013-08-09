package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class RoleDefinition extends PrimaryObject {

	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	public static final String F_ORGANIZATION_ROLE_ID = "role_id";

	public static final String F_ROLE_NUMBER = "rolenumber";

	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";

	public boolean isOrganizatioRole() {
		return getValue(F_ORGANIZATION_ROLE_ID)!=null;
	}

	public Image getImage() {
		if (isOrganizatioRole()) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE4_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE3_16);
		}
	}
}
