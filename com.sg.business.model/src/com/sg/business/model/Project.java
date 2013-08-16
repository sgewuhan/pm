package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;


public class Project extends PrimaryObject{

	
	/**
	 * 项目负责人字段，保存项目负责人的userid {@link User} , 
	 */
	public static final String F_CHARGER = "charger";
	
	/**
	 * 数组类型字段，字段中的每个元素为 userData 
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * 项目所属的项目职能组织
	 */
	public static final String FUNCTION_ORGANIZATION = "organization_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}
	
}
