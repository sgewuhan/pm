package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

public class Project extends PrimaryObject {

	/**
	 * 项目负责人字段，保存项目负责人的userid {@link User} ,
	 */
	public static final String F_CHARGER = "chargerid";

	/**
	 * 数组类型字段，字段中的每个元素为 userData
	 */
	public static final String F_PARTICIPATE = "participate";

	/**
	 * 项目所属的项目职能组织
	 */
	public static final String F_FUNCTION_ORGANIZATION = "org_id";

	/**
	 * 项目发起部门
	 */
	public static final String F_LAUNCH_ORGANIZATION = "launchorg_id";

	public static final String F_PROJECT_TEMPLATE_ID = "projecttemplate_id";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16);
	}

	public User getCharger() {
		String chargerId = (String) getValue(F_CHARGER);
		if (Utils.isNullOrEmpty(chargerId)) {
			return null;
		}
		return User.getUserById(chargerId);
	}

	public Organization getFunctionOrganization() {
		ObjectId orgId = getFunctionOrganizationId();
		if (orgId != null) {
			return ModelService.createModelObject(Organization.class,
					(ObjectId) orgId);
		}
		return null;
	}

	public ObjectId getFunctionOrganizationId() {
		return (ObjectId) getValue(F_FUNCTION_ORGANIZATION);
	}

	public ProjectTemplate getProjectTemplate() {
		ObjectId id = (ObjectId) getValue(F_PROJECT_TEMPLATE_ID);
		if(id!=null){
			return ModelService.createModelObject(ProjectTemplate.class, id);
		}
		return null;
	}

}
