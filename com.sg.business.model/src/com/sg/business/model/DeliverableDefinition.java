package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class DeliverableDefinition extends PrimaryObject {

	/**
	 * �����������Ĺ���
	 */
	public static final String F_WORKD_ID = "workd_id";

	/**
	 * ������ı༭��ID,����plugins.xml����һ��
	 */
	public static final String EDITOR = "editor.deliverableDefinition";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}
}
