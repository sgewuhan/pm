package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * 角色定义<p/>
 * 在项目模板和项目中定义的角色
 * @author zhonghua
 *
 */
public class AbstractRoleDefinition extends PrimaryObject{

	/**
	 * 角色ID
	 */
	public static final String F_ORGANIZATION_ROLE_ID = "role_id";

	/**
	 * 角色编号
	 */
	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * 编辑角色的编辑器
	 */
	public static final String EDITOR_ROLE_DEFINITION_CREATE = "editor.roleDefinition";

	/**
	 * 判断角色是否为组织角色
	 * @return boolean
	 */
	public boolean isOrganizatioRole() {
		return getValue(F_ORGANIZATION_ROLE_ID) != null;
	}

	/**
	 * 获取角色的显示图标
	 * @return Image
	 */
	public Image getImage() {
		if (isOrganizatioRole()) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE4_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE3_16);
		}
	}

	/**
	 * 获取组织角色，如果组织中不存在此角色定义就创建
	 * @return
	 */
	public Role getOrganizationRole() {
		ObjectId id = getOrganizationRoleId();
		Assert.isNotNull(id);
		return ModelService.createModelObject(Role.class, id);
	}

	public ObjectId getOrganizationRoleId() {
		return (ObjectId) getValue(F_ORGANIZATION_ROLE_ID);
	}

	/**
	 * 角色的显示图标
	 * @return String
	 */
	@Override
	public String getLabel() {
		if (isOrganizatioRole()) {
			Role role = getOrganizationRole();
			return role.getLabel();
		} else {
			return getDesc() + "|" + getRoleNumber();
		}
	}

	/**
	 * 获取角色的编号
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

}
