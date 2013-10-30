package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.event.AccountEvent;
import com.sg.business.resource.BusinessResource;

/**
 * 角色
 * <p/>
 * 组织中的角色
 * 
 * @author jinxitao
 * 
 */
public class Role extends PrimaryObject {

	/**
	 * 所属组织ID
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 角色编号
	 */
	public static final String F_ROLE_NUMBER = "rolenumber";

	/**
	 * 默认编辑器
	 */
	public static final String EDITOR_DEFAULT = "editor.organization.role";

	/**
	 * 基础角色/组织角色/系统管理员
	 */
	public static final String ROLE_ADMIN_ID = "T000";
	public static final String ROLE_ADMIN_TEXT = "系统管理员";

	/**
	 * 基础角色/组织角色/组织管理员
	 */
	@Deprecated
	public static final String ROLE_ORGANIZATION_ADMIN_ID = "T000";
	@Deprecated
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
	 * 基础角色/组织角色/部门经理
	 */
	public static final String ROLE_DEPT_MANAGER_ID = "T005";
	public static final String ROLE_DEPT_MANAGER_TEXT = "管理者";

	/**
	 * 基础角色/组织角色/财务经理，该角色只能出现在事业部级别的组织
	 */
	public static final String ROLE_FINANCIAL_MANAGER_ID = "T006";
	public static final String ROLE_FINANCIAL_MANAGER_TEXT = "财务经理";
	
	
	public static final String ROLE_CHIEF_ENGINEER_ID = "Chief Engineer";
	public static final String ROLE_CHIEF_ENGINEER_TEXT = "总工";
	
	public static final String ROLE_PROJECR_APPROVER_ID = "Project Approver";
	public static final String ROLE_PROJECR_APPROVER_TEXT = "立项批准者";
	
	public static final String ROLE_DEPUTY_DIRECTOR_ID = "Deputy Director";
	public static final String ROLE_DEPUTY_DIRECTOR_TEXT = "常务副主任";
	
	public static final String ROLE_DIRECTOR_ID = "Director";
	public static final String ROLE_DIRECTOR_TEXT = "研究室主任";
	
	public static final String ROLE_CHIEF_MASTER_ID = "Chief Master";
	public static final String ROLE_CHIEF_MASTER_TEXT = "首席师";
	
	public static final String ROLE_DATAAUDIT_ID = "Data Audit";
	public static final String ROLE_DATAAUDIT_TEXT = "资料审核员";
	
	public static final String ROLE_DEVELOPMENTDIRECTOR_ID = "Development Director";
	public static final String ROLE_DEVELOPMENTDIRECTOR_TEXT = "开发中心主任";


	/**
	 * 系统角色ID
	 */
	public static final String[] ROLE_ID_SYSTEM = new String[] { ROLE_ADMIN_ID,
			ROLE_ORGANIZATION_ADMIN_ID, ROLE_PROJECT_ADMIN_ID,
			ROLE_BUSINESS_ADMIN_ID, ROLE_VAULT_ADMIN_ID, ROLE_VAULT_GUEST_ID,
			ROLE_DEPT_MANAGER_ID, ROLE_FINANCIAL_MANAGER_ID };

	/**
	 * 系统角色名称
	 */
	public static final String[] ROLE_NAME_SYSTEM = new String[] {
			ROLE_ADMIN_TEXT, ROLE_ORGANIZATION_ADMIN_TEXT,
			ROLE_BUSINESS_ADMIN_TEXT, ROLE_PROJECT_ADMIN_TEXT,
			ROLE_VALUT_ADMIN_TEXT, ROLE_VAULT_GUEST_TEXT,
			ROLE_DEPT_MANAGER_TEXT, ROLE_FINANCIAL_MANAGER_TEXT };

	

	

	

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
	 * 获取角色所属组织的_id
	 * 
	 * @return ObjectId
	 */
	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * 添加用户到角色中
	 * 
	 * @param users
	 *            ，用户集合
	 * @throws Exception
	 */
	public void doAssignUsers(List<PrimaryObject> users, IContext context)
			throws Exception {
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);
		List<DBObject> list = new ArrayList<DBObject>();

		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			if (!user.isActivated()) {
				continue;
			}
			list.add(new BasicDBObject()
					.append(RoleAssignment.F__TYPE,
							IModelConstants.C_ROLE_ASSIGNMENT)
					.append(RoleAssignment.F_USER_ID, user.getUserid())
					.append(RoleAssignment.F_USER_NAME, user.getUsername())
					.append(RoleAssignment.F_ROLE_ID, get_id())
					.append(RoleAssignment.F_ROLE_NUMBER, getRoleNumber())
					.append(RoleAssignment.F_ROLE_NAME, getDesc()));
		}
		roleAssignmentCol.insert(list);

		// 通知帐户
		for (int i = 0; i < users.size(); i++) {
			User user = (User) users.get(i);
			UserSessionContext.noticeAccountChanged(user.getUserid(),
					new AccountEvent(AccountEvent.EVENT_ROLE_CHANGED, this));
		}

		DBUtil.SAVELOG(context.getAccountInfo().getUserId(), "为角色指派用户",
				new Date(), "角色：" + this + "\n用户" + users.toString(),
				IModelConstants.DB);
	}

	public List<PrimaryObject> getAssignment() {
		return getRelationById(F__ID, RoleAssignment.F_ROLE_ID,
				RoleAssignment.class);
	}

	/**
	 * 删除角色检查
	 */
	public List<Object[]> checkRemoveAction(IContext context) {
		List<Object[]> message = new ArrayList<Object[]>();
		// 1.角色项下的用户
		long countUser = getRelationCountByCondition(RoleAssignment.class,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, get_id()));
		if (countUser > 0) {
			message.add(new Object[] { "该角色项下存在指派的用户", this, SWT.ICON_ERROR });
		}

		// 2.项目模板的RoleDefinition引用的角色
		long countRoleDefinition = getRelationCountByCondition(
				RoleAssignment.class, new BasicDBObject().append(
						RoleDefinition.F_ORGANIZATION_ROLE_ID, get_id()));
		if (countRoleDefinition > 0) {
			message.add(new Object[] { "在项目模版中引用了该角色", this, SWT.ICON_ERROR });
		}

		// 3.独立工作定义的WBS引用的角色
		BasicDBList values = new BasicDBList();
		values.add(new BasicDBObject().append(
				WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(WorkDefinition.F_CHARGER_ROLE_ID,
				get_id()));
		values.add(new BasicDBObject().append(
				WorkDefinition.F_PARTICIPATE_ROLE_SET, Pattern.compile("^.*"
						+ get_id() + ".*$", Pattern.CASE_INSENSITIVE)));
		long countWorkDefinition = getRelationCountByCondition(
				WorkDefinition.class,
				new BasicDBObject().append("$or", values).append(
						WorkDefinition.F_WORK_TYPE,
						WorkDefinition.WORK_TYPE_STANDLONE));
		if (countWorkDefinition > 0) {
			message.add(new Object[] { "在独立工作定义中引用了该角色", this, SWT.ICON_ERROR });
		}

		// 4.项目ProjectRole引用的角色
		long countProjectRole = getRelationCountByCondition(ProjectRole.class,
				new BasicDBObject().append(ProjectRole.F_ORGANIZATION_ROLE_ID,
						get_id()));
		if (countProjectRole > 0) {
			message.add(new Object[] { "在项目中引用了该角色", this, SWT.ICON_ERROR });
		}

		// 5.独立工作的WBS引用的角色
		values.clear();
		values.add(new BasicDBObject().append(
				Work.F_ASSIGNMENT_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(Work.F_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(Work.F_PARTICIPATE_ROLE_SET,
				Pattern.compile("^.*" + get_id() + ".*$",
						Pattern.CASE_INSENSITIVE)));
		long countWork = getRelationCountByCondition(
				Work.class,
				new BasicDBObject().append("$or", values).append(
						Work.F_PROJECT_ID, null));
		if (countWork > 0) {
			message.add(new Object[] { "在独立工作中引用了该角色", this, SWT.ICON_WARNING });
		}

		return message;
	}

	/**
	 * 删除角色
	 * 
	 * @param context
	 */
	@Override
	public void doRemove(IContext context) throws Exception {
		// 需要考虑角色被应用到项目的情况
		// 先删除角色指派
		DBCollection raCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE_ASSIGNMENT);
		raCol.remove(new BasicDBObject().append(RoleAssignment.F_ROLE_ID,
				get_id()));
		super.doRemove(context);
	}

	/**
	 * 获取角色编号
	 * 
	 * @return String
	 */
	public String getRoleNumber() {
		return (String) getValue(F_ROLE_NUMBER);
	}

	/**
	 * 获得角色从属的组织
	 * 
	 * @return Organization
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
	 * 获取角色类型,角色类型分为系统角色和用户自定义角色
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @return boolean
	 */
	public boolean isSystemRole() {
		String rn = getRoleNumber();
		if (Utils.inArray(rn, ROLE_ID_SYSTEM)) {
			// 检查与组织容器属性相关的角色
			if (ROLE_VAULT_ADMIN_ID.equals(rn)
					|| ROLE_VAULT_GUEST_ID.equals(rn)) {
				// 判断该组织是否是容器
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isContainer())) {
					return true;
				} else {
					return false;
				}
			} else if (ROLE_PROJECT_ADMIN_ID.equals(rn)
					|| ROLE_BUSINESS_ADMIN_ID.equals(rn)) {
				// 判断该组织是否具有项目管理职能
				Organization org = getOrganization();
				if (Boolean.TRUE.equals(org.isFunctionDepartment())) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
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
				&& (rn.toUpperCase().startsWith("T0") || rn.toUpperCase()
						.startsWith("P0"));
	}

	/**
	 * 判断角色编号是否合法 角色是否为系统保留编号，角色所属组织是否存在，角色在所属组织中是否已经存在
	 * 
	 * @throws Exception
	 */
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

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "角色";
	}

	@Override
	public String getDefaultEditorId() {
		return EDITOR_DEFAULT;
	}
}
