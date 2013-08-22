package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class Deliverable extends PrimaryObject implements IProjectRelative{

	public static final String F_WORK_ID = "work_id";
	public static final String F_MANDATORY = "mandatory";
	public static final String F_DOCUMENT_ID = "document_id";
	public static final String EDITOR = null;

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}
	
	@Override
	public String getTypeName() {
		return "½»¸¶Îï";
	}

}
