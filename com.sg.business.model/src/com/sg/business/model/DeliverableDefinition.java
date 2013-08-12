package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class DeliverableDefinition extends PrimaryObject {

	/**
	 * 交付物所属的工作
	 */
	public static final String F_WORKD_ID = "workd_id";

	/**
	 * 交付物的编辑器ID,请与plugins.xml保持一致
	 */
	public static final String EDITOR = "editor.deliverableDefinition";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}
}
