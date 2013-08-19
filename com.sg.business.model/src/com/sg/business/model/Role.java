package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

/**
 * 角色<p/>
 * 组织中的角色
 * @author zhonghua
 *
 */
public class Role extends PrimaryObject {

	public static final String F_ORGANIZATION_ID = "organization_id";

	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * 基础角色/组织角色/系统管理员
	 */
	public static final String ROLE_ADMIN_ID = "T000";
	public static final String ROLE_ADMIN_TEXT = "系统管理员";

	/**
	 * 基础角色/组织角色/组织管理员
	 */
	public static final String ROLE_ORGANIZATION_ADMIN_ID = "T000";
	public static final String ROLE_ORGANIZATION_ADMIN_TEXT = "组织管理员";

	/**
	 * 基础角色/组织角色/文档管理员
	 */
	public static final String ROLE_VAULT_ADMIN_ID = "T001";
	public static final String ROLE_VALUT_ADMIN_TEXT = "文档管理员";

	/**
	 * 基础角色/组织角色/项目管理员
	 */
	public static final String ROLE_PROJECT_ADMIN_ID = "T002";
	public static final String ROLE_PROJECT_ADMIN_TEXT = "项目管理员";

	/**
	 * 基础角色/组织角色/业务管理员
	 */
	public static final String ROLE_BUSINESS_ADMIN_ID = "T004";
	public static final String ROLE_BUSINESS_ADMIN_TEXT = "业务管理员";
	
	/**
	 * 基础角色/组织角色/文档访问者
	 */
	public static final String ROLE_VAULT_GUEST_ID = "T003";
	public static final String ROLE_VAULT_GUEST_TEXT = "文档访问者";

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
	public static final String[] ROLE_ID_SYSTEM = new String[] { ROLE_ADMIN_ID,
			ROLE_ORGANIZATION_ADMIN_ID, ROLE_PROJECT_ADMIN_ID,ROLE_BUSINESS_ADMIN_ID,
			ROLE_PROJECT_GUEST_ID, ROLE_PROJECT_MANAGER_ID, ROLE_VAULT_ADMIN_ID,
			ROLE_VAULT_GUEST_ID };

	/**
	 * 系统角色名称
	 */
	public static final String[] ROLE_NAME_SYSTEM = new String[] {
			ROLE_ADMIN_TEXT, ROLE_ORGANIZATION_ADMIN_TEXT,ROLE_BUSINESS_ADMIN_TEXT,
			ROLE_PROJECT_ADMIN_TEXT, ROLE_PROJECT_GUEST_TEXT,
			ROLE_PROJECT_MANAGER_TEXT, ROLE_VALUT_ADMIN_TEXT,
			ROLE_VAULT_GUEST_TEXT };

	/**
	 * 角色在系统中的的显示内容
	 */
	@Override
	public String getLabel() {
		return getDesc() + "|" + getRoleNumber();
	}

	/**
	 * 角色在系统中的显示图标
	 */
	@Override
	public Image getImage() {
		if (isSystemRole()) {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE2_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_ROLE_16);
		}
	}

	/**
	 * 获取角色所属组织的ID
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * 添加用户到角色中
	 * @param users
	 *          ，用户集合
	 */
	public void doAssignUsers(List<PrimaryObject> users) {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();

		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					UserSessionContext.EVENT_ROLE_CHANGED);
			list.add(new BasicDBObject()
					.append(RoleAssignment.F__TYPE,IModelConstants.C_ROLE_ASSIGNMENT)
					.append(RoleAssignment.F_USER_ID, user.getUserid())
					.append(RoleAssignment.F_USER_NAME, user.getUsername())
					.append(RoleAssignment.F_ROLE_ID, get_id())
					.append(RoleAssignment.F_ROLE_NUMBER, getRoleNumber())
					.append(RoleAssignment.F_ROLE_NAME, getDesc()));
		}
		roleAssignmentCol.insert(list);

	}

	/**
	 * 删除角色
	 * @param context
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// 先删除角色指派
		DBCollection raCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_ASSIGNMENT);
		raCol.remove(new BasicDBObject().append(RoleAssignment.F_ROLE_ID,
				get_id()));
		super.doRemove(context);
	}

	/**
	 * 获取角色编号
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

	/**
	 * 获得角色从属的组织
	 * 
	 * @return  Organization
	 */
	public Organization getOrganization() {
		ObjectId organization_id = getOrganization_id();
		if (organization_id != null) {
			return ModelService.createModelObject(Organization.class,
					organization_id);
		} else {
			return null;
		}
	}

	/**
	 * 获取角色类型
	 * @return
	 */
	public String getRoleTypeText() {
		if (isSystemRole()) {
			return "系统角色";
		} else {
			return "用户角色";
		}
	}

	/**
	 * 判断角色是否可以更改
	 * @param context
	 */
	@Override
	public boolean canEdit(IContext context) {
		// 系统的角色不可以更改
		if (isSystemRole()) {
			return false;
		}
		return super.canEdit(context);
	}

	/**
	 * 判断角色是否可以删除
	 * @param context
	 * @return boolean
	 */
	@Override
	public boolean canDelete(IContext context) {
		if (isSystemRole()) {
			return false;
		}
		return super.canDelete(context);
	}

	/**
	 * 判断角色是否为系统角色
	 * @return boolean
	 */
	public boolean isSystemRole() {
		String rn = getRoleNumber();
		if (Utils.inArray(rn, ROLE_ID_SYSTEM)) {
			// 检查与组织容器属性相关的角色
			if (ROLE_VAULT_ADMIN_ID.equals(rn) || ROLE_VAULT_GUEST_ID.equals(rn)) {
				// 判断该组织是否是容器
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isContainer())) {
					return true;
				} else {
					return false;
				}
			} else if (ROLE_PROJECT_ADMIN_ID.equals(rn)|| ROLE_BUSINESS_ADMIN_ID.equals(rn)) {
				// 判断该组织是否具有项目管理职能
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isFunctionDepartment())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * T开头和P开头的是系统预留的角色名
	 * 
	 * @param rn
	 * @return
	 */
	private boolean isReservedNumber(String rn) {
		return rn != null
				&& (rn.toUpperCase().startsWith("T") || rn.toUpperCase()
						.startsWith("P"));
	}

	public void check() throws Exception {
		String rn = getRoleNumber();
		// 检查角色编号是否合法
		if (isReservedNumber(rn)) {
			throw new Exception("输入的角色编号是系统保留的编号");
		}
		// 检查角色编号在当前组织中是否存在
		Organization org = getOrganization();
		if (org == null) {
			throw new Exception("角色所属的组织已不存在");
		}
		if (org.hasRole(rn)) {
			throw new Exception("角色编号在所属的组织中重复");
		}

	}
}
