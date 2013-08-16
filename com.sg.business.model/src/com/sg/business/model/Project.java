package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;


public class Project extends PrimaryObject{

	
	/**
	 * ��Ŀ�������ֶΣ�������Ŀ�����˵�userid {@link User} , 
	 */
	public static final String F_CHARGER = "charger";
	
	/**
	 * ���������ֶΣ��ֶ��е�ÿ��Ԫ��Ϊ userData 
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * ��Ŀ��������Ŀְ����֯
	 */
	public static final String FUNCTION_ORGANIZATION = "organization_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}
	
}
