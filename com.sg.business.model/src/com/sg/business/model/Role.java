package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.javascript.JavaScriptEvaluator;
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
import com.sg.business.resource.nls.Messages;

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
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$

	/**
	 * 角色编号
	 */
	public static final String F_ROLE_NUMBER = "rolenumber"; //$NON-NLS-1$

	/**
	 * 默认编辑器
	 */
	public static final String EDITOR_DEFAULT = "editor.organization.role"; //$NON-NLS-1$

	/**
	 * 基础角色/组织角色/系统管理员
	 */
	public static final String ROLE_ADMIN_ID = "T000"; //$NON-NLS-1$
	public static final String ROLE_ADMIN_TEXT = Messages.get().Role_4;

	/**
	 * 基础角色/组织角色/组织管理员
	 */
	public static final String ROLE_ORGANIZATION_ADMIN_ID = "T000"; //$NON-NLS-1$
	public static final String ROLE_ORGANIZATION_ADMIN_TEXT = Messages.get().Role_6;

	/**
	 * 基础角色/组织角色/文档管理员
	 */
	public static final String ROLE_VAULT_ADMIN_ID = "T001"; //$NON-NLS-1$
	public static final String ROLE_VALUT_ADMIN_TEXT = Messages.get().Role_8;

	/**
	 * 基础角色/组织角色/项目管理员
	 */
	public static final String ROLE_PROJECT_ADMIN_ID = "T002"; //$NON-NLS-1$
	public static final String ROLE_PROJECT_ADMIN_TEXT = Messages.get().Role_10;

	/**
	 * 基础角色/组织角色/业务管理员
	 */
	public static final String ROLE_BUSINESS_ADMIN_ID = "T004"; //$NON-NLS-1$
	public static final String ROLE_BUSINESS_ADMIN_TEXT = Messages.get().Role_12;

	/**
	 * 基础角色/组织角色/文档访问者
	 */
	public static final String ROLE_VAULT_GUEST_ID = "T003"; //$NON-NLS-1$
	public static final String ROLE_VAULT_GUEST_TEXT = Messages.get().Role_14;

	/**
	 * 基础角色/组织角色/部门经理
	 */
	public static final String ROLE_DEPT_MANAGER_ID = "T005"; //$NON-NLS-1$
	public static final String ROLE_DEPT_MANAGER_TEXT = Messages.get().Role_16;

	/**
	 * 基础角色/组织角色/财务经理，该角色只能出现在事业部级别的组织
	 */
	public static final String ROLE_FINANCIAL_MANAGER_ID = "T006"; //$NON-NLS-1$
	public static final String ROLE_FINANCIAL_MANAGER_TEXT = Messages.get().Role_18;

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

	public static final String ROLE_ASSIGNMENT_ID = "T007"; //$NON-NLS-1$
	public static final String ROLE_ASSIGNMENT_TEXT = Messages.get().Role_20;

	public static final String F_RULE = "rule";

	/**
	 * 角色在系统中的的显示内容
	 */
	@Override
	public String getLabel() {
		return getDesc() + "|" + getRoleNumber(); //$NON-NLS-1$
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

		DBUtil.SAVELOG(context.getAccountInfo().getUserId(),
				Messages.get().Role_22, new Date(), Messages.get().Role_23
						+ this + Messages.get().Role_24 + users.toString(),
				IModelConstants.DB);
	}

	public List<PrimaryObject> getAssignment() {
		return getRelationById(F__ID, RoleAssignment.F_ROLE_ID,
				RoleAssignment.class);
	}

	public List<PrimaryObject> getAssignment(Map<String, Object> parameters) {
		String js = getStringValue(F_RULE);
		List<PrimaryObject> rs = getRelationById(F__ID,
				RoleAssignment.F_ROLE_ID, RoleAssignment.class);
		parameters.put(RoleParameter.ASSIGNMENT, rs);
		//转换处理
		Object projectId = parameters.get(RoleParameter.PROJECT_ID);
		if(projectId instanceof ObjectId){
			Project project = ModelService.createModelObject(Project.class, (ObjectId)projectId);
			parameters.put(RoleParameter.PROJECT, project);
		}
		if (js != null) {
			Object result = JavaScriptEvaluator.evaluate(js, parameters);
			if (result instanceof String[]) {
				ArrayList<PrimaryObject> rs1 = new ArrayList<PrimaryObject>();
				for (int i = 0; i < rs.size(); i++) {
					Object userid = rs.get(i)
							.getValue(RoleAssignment.F_USER_ID);
					if (Utils.inArray(userid, (String[]) result)) {
						rs1.add(rs.get(i));
					}
				}
				return rs1;
			} else if (result instanceof String) {
				ArrayList<PrimaryObject> rs1 = new ArrayList<PrimaryObject>();
				for (int i = 0; i < rs.size(); i++) {
					Object userid = rs.get(i)
							.getValue(RoleAssignment.F_USER_ID);
					if (Util.equals(userid, result)) {
						rs1.add(rs.get(i));
					}
				}
				return rs1;
			}
		}
		return rs;
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
			message.add(new Object[] { Messages.get().Role_25, this,
					SWT.ICON_ERROR });
		}

		// 2.项目模板的RoleDefinition引用的角色
		long countRoleDefinition = getRelationCountByCondition(
				RoleAssignment.class, new BasicDBObject().append(
						RoleDefinition.F_ORGANIZATION_ROLE_ID, get_id()));
		if (countRoleDefinition > 0) {
			message.add(new Object[] { Messages.get().Role_26, this,
					SWT.ICON_ERROR });
		}

		// 3.独立工作定义的WBS引用的角色
		BasicDBList values = new BasicDBList();
		values.add(new BasicDBObject().append(
				WorkDefinition.F_ASSIGNMENT_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(WorkDefinition.F_CHARGER_ROLE_ID,
				get_id()));
		values.add(new BasicDBObject().append(
				WorkDefinition.F_PARTICIPATE_ROLE_SET, Pattern.compile("^.*" //$NON-NLS-1$
						+ get_id() + ".*$", Pattern.CASE_INSENSITIVE))); //$NON-NLS-1$
		long countWorkDefinition = getRelationCountByCondition(
				WorkDefinition.class,
				new BasicDBObject().append("$or", values).append( //$NON-NLS-1$
						WorkDefinition.F_WORK_TYPE,
						WorkDefinition.WORK_TYPE_STANDLONE));
		if (countWorkDefinition > 0) {
			message.add(new Object[] { Messages.get().Role_30, this,
					SWT.ICON_ERROR });
		}

		// 4.项目ProjectRole引用的角色
		long countProjectRole = getRelationCountByCondition(ProjectRole.class,
				new BasicDBObject().append(ProjectRole.F_ORGANIZATION_ROLE_ID,
						get_id()));
		if (countProjectRole > 0) {
			message.add(new Object[] { Messages.get().Role_31, this,
					SWT.ICON_ERROR });
		}

		// 5.独立工作的WBS引用的角色
		values.clear();
		values.add(new BasicDBObject().append(
				Work.F_ASSIGNMENT_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(Work.F_CHARGER_ROLE_ID, get_id()));
		values.add(new BasicDBObject().append(Work.F_PARTICIPATE_ROLE_SET,
				Pattern.compile("^.*" + get_id() + ".*$", //$NON-NLS-1$ //$NON-NLS-2$
						Pattern.CASE_INSENSITIVE)));
		long countWork = getRelationCountByCondition(Work.class,
				new BasicDBObject().append("$or", values).append( //$NON-NLS-1$
						Work.F_PROJECT_ID, null));
		if (countWork > 0) {
			message.add(new Object[] { Messages.get().Role_35, this,
					SWT.ICON_WARNING });
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
			return Messages.get().Role_36;
		} else {
			return Messages.get().Role_37;
		}
	}

	/**
	 * 判断角色是否可以更改
	 * 
	 * @param context
	 */
	@Override
	public boolean canEdit(IContext context) {
//		String uid = context.getAccountInfo().getUserId();
//		User user = UserToolkit.getUserById(uid);
//		if (Boolean.TRUE.equals(user.getValue(User.F_IS_ADMIN))) {
//			return true;
//		}
//		// 系统的角色不可以更改
//		if (isSystemRole()) {
//			return false;
//		}
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
				&& (rn.toUpperCase().startsWith("T0") || rn.toUpperCase() //$NON-NLS-1$
						.startsWith("P0")); //$NON-NLS-1$
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
			throw new Exception(Messages.get().Role_40);
		}
		// 检查角色编号在当前组织中是否存在
		Organization org = getOrganization();
		if (org == null) {
			throw new Exception(Messages.get().Role_41);
		}
		if (org.hasRole(rn)) {
			throw new Exception(Messages.get().Role_42);
		}

	}

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().Role_43;
	}

	@Override
	public String getDefaultEditorId() {
		return EDITOR_DEFAULT;
	}
}
