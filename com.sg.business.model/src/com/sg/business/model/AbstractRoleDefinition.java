package com.sg.business.model;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * 角色定义
 * <p/>
 * 在项目模板和项目中定义的角色
 * 
 * @author zhonghua
 * 
 */
public abstract class AbstractRoleDefinition extends PrimaryObject {

	/**
	 * 基础角色/项目角色/项目经理
	 */
	public static final String ROLE_PROJECT_MANAGER_ID = "P000";
	public static final String ROLE_PROJECT_MANAGER_TEXT = "项目经理";

	/**
	 * 基础角色/项目角色/项目访问者
	 */
	public static final String ROLE_PROJECT_GUEST_ID = "P001";
	public static final String ROLE_PROJECT_GUEST_TEXT = "项目访问者";

	/**
	 * 系统角色ID
	 */
	public static final String[] ROLE_ID_SYSTEM = new String[] {
			ROLE_PROJECT_MANAGER_ID, ROLE_PROJECT_GUEST_ID };

	/**
	 * 系统角色名称
	 */
	public static final String[] ROLE_NAME_SYSTEM = new String[] {
			ROLE_PROJECT_MANAGER_TEXT, ROLE_PROJECT_GUEST_TEXT };

	/**
	 * 角色ID
	 */
	public static final String F_ORGANIZATION_ROLE_ID = "role_id";

	/**
	 * 角色编号
	 */
	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * 判断角色是否为组织角色
	 * 
	 * @return boolean
	 */
	public boolean isOrganizatioRole() {
		return getValue(F_ORGANIZATION_ROLE_ID) != null;
	}

	/**
	 * 获取角色的显示图标
	 * 
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
	 * 
	 * 返回角色的组织角色
	 * 
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
	 * 角色的显示内容
	 * 
	 * @return String
	 */
	@Override
	public String getLabel() {
		if (isPersistent()) {
			if (isOrganizatioRole()) {
				Role role = getOrganizationRole();
				return role.getLabel();
			} else {
				return getDesc() + "|" + getRoleNumber();
			}
		} else {
			return "";
		}
	}

	/**
	 * 获取角色的编号
	 * 
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

	/**
	 * 判断角色是否为系统角色
	 * 
	 * @return boolean
	 */
	public boolean isSystemRole() {
		String rn = getRoleNumber();
		return Utils.inArray(rn, ROLE_ID_SYSTEM);
	}

	/**
	 * T开头和P开头的是系统预留的角色名
	 * 
	 * @param rn
	 * @return
	 */
	public boolean isReservedNumber(String rn) {
		return rn != null
				&& (rn.toUpperCase().startsWith("T0") || rn.toUpperCase()
						.startsWith("P0"));
	}

	/**
	 * 检查角色编号是否合法
	 * @throws Exception 
	 */
	public void check() throws Exception {
		
		// [bug:18] 连带处理
		String rn = getRoleNumber();
		if (isReservedNumber(rn)) {
			throw new Exception("输入的角色编号是系统保留的编号");
		}
		
	}

}
