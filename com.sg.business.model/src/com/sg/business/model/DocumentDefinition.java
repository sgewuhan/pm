package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class DocumentDefinition extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organization_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DOCUMENT_DEF_16);
	}
}
