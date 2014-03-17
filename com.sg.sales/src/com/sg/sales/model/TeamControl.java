package com.sg.sales.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.BasicBSONList;
import org.eclipse.core.runtime.Assert;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleAssignment;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;

public abstract class TeamControl extends OrganizationControl {

	public static final String F_VISITORLIST = "visitor_list";
	public static final String F_PERMISSION_OWNERLIST = "owner_list";
	public static final String F_OWNER = "owner";
	protected static final String MESSAGE_NOT_PERMISSION = "您没有获得操作的权限";

	@Override
	public boolean canDelete(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		return checkUser(new String[] { F_OWNER, F_PERMISSION_OWNERLIST },
				userId);
	}

	@Override
	public boolean canEdit(IContext context) {
		if (!isPersistent()) {
			return true;
		}
		String userId = context.getAccountInfo().getConsignerId();
		return checkUser(new String[] { F_OWNER, F_PERMISSION_OWNERLIST },
				userId);
	}

	@Override
	public boolean canRead(IContext context) {
		String userId = context.getAccountInfo().getConsignerId();
		return checkUser(new String[] { F_OWNER, F_VISITORLIST,
				F_PERMISSION_OWNERLIST }, userId);
	}

	// 获得我可以访问的客户的条件
	public static BasicDBObject getOwnerCondition(String userId) {
		Object cond0 = new BasicDBObject().append(F_OWNER, userId);
		Object cond5 = new BasicDBObject().append(F_PERMISSION_OWNERLIST,
				userId);
		return new BasicDBObject().append("$or", new Object[] { cond0, cond5 });
	}

	public static BasicDBObject getVisitableCondition(String userId) {
		Object cond0 = new BasicDBObject().append(F_OWNER, userId);
		Object cond5 = new BasicDBObject().append(F_VISITORLIST, userId);
		Object cond6 = new BasicDBObject().append(F_PERMISSION_OWNERLIST,
				userId);
		return new BasicDBObject().append("$or", new Object[] { cond0, cond5,
				cond6 });
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		ensureTeamControled(context);
		return super.doSave(context);
	}

	/**
	 * 确保可以访问和编辑的人员
	 * 
	 * @param context
	 */
	protected void ensureTeamControled(IContext context) {
		String currentUserId = context.getAccountInfo().getUserId();
		// 确保 owner
		String ownerUserId = (String) getValue(F_OWNER);
		if (ownerUserId == null) {
			ownerUserId = currentUserId;
		}
		setValue(F_OWNER, ownerUserId);
		// 确保访问者
		Object value = getValue(F_VISITORLIST);
		if (!(value instanceof BasicBSONList)) {
			value = new BasicDBList();
		}
		BasicBSONList visitable = (BasicBSONList) value;
		String[] visitableFields = getVisitableFields();
		if (visitableFields != null && visitableFields.length > 0) {
			for (int i = 0; i < visitableFields.length; i++) {
				Object userId = getValue(visitableFields[i]);
				if (userId != null) {
					if (!visitable.contains(userId)) {
						visitable.add(userId);
					}
				}
			}
		}
		setValue(F_VISITORLIST, visitable);

		value = getValue(F_PERMISSION_OWNERLIST);
		if (!(value instanceof BasicBSONList)) {
			value = new BasicDBList();
		}
		BasicBSONList ownerable = (BasicBSONList) value;
		// 确保授权者在OwnerList中
		// 寻找owner的授权者角色
		String roleNumber = getPermissionRoleNumber();
		if (roleNumber != null) {
			User owner = UserToolkit.getUserById(ownerUserId);
			Organization org = owner.getOrganization();
			if (org != null) {
				Role role = org
						.getRole(roleNumber, Organization.ROLE_SEARCH_UP);
				if (role != null) {
					List<PrimaryObject> ralist = role.getAssignment();
					if (ralist != null) {
						for (int i = 0; i < ralist.size(); i++) {
							RoleAssignment ra = (RoleAssignment) ralist.get(i);
							String userId = ra.getUserid();
							if (!ownerable.contains(userId)) {
								ownerable.add(userId);
							}
						}
					}
				}
			}
		}
		setValue(F_PERMISSION_OWNERLIST, ownerable);

		// 确保一些字段按角色分配用户
		String[] fields = getRoleDesignatedUserFieldName();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				Object userId = getValue(fields[i]);
				if(userId!=null){
					continue;
				}
				roleNumber = getRoleNumberDesignatedUserField(fields[i]);
				if (roleNumber == null) {
					continue;
				}
				User user = UserToolkit.getUserById(ownerUserId);
				Organization org = user.getOrganization();
				if (org == null) {
					continue;
				}
				Role role = org
						.getRole(roleNumber, Organization.ROLE_SEARCH_UP);
				if (role == null) {
					continue;
				}
				List<PrimaryObject> ralist = role.getAssignment();
				if (ralist == null || ralist.isEmpty()) {
					continue;
				}
				RoleAssignment ra = (RoleAssignment) ralist.get(0);
				userId = ra.getUserid();
				setValue(fields[i], userId);
			}
		}
	}

	protected String getRoleNumberDesignatedUserField(String field) {
		return null;
	}

	/**
	 * 返回需要按角色设置字段名称
	 * 
	 * @return
	 */
	protected String[] getRoleDesignatedUserFieldName() {
		return null;
	}

	protected abstract String getPermissionRoleNumber();

	protected abstract String[] getVisitableFields();

	protected abstract String[] getDuplicateTeamFields();

	protected boolean checkUser(String[] fields, String userId) {
		for (int i = 0; i < fields.length; i++) {
			Object value = getValue(fields[i]);
			if (userId.equals(value)) {
				return true;
			} else {
				if ((value instanceof List)
						&& (((List<?>) value).contains(userId))) {
					return true;
				}
			}
		}
		return false;
	}

	public void duplicateTeamTo(PrimaryObject to) {
		String[] fs = getDuplicateTeamFields();
		if (fs != null) {
			for (int i = 0; i < fs.length; i++) {
				to.setValue(fs[i], getValue(fs[i]));
			}
		}
	}

	public void checkAndDuplicateTeamFrom(TeamControl from) {
		String[] fs = getDuplicateTeamFields();
		if (fs != null) {
			for (int i = 0; i < fs.length; i++) {
				if (getValue(fs[i]) != null) {
					return;
				}
			}
		}

		from.duplicateTeamTo(this);
	}

	public List<User> getPermissionOwnerList() {
		Object value = getValue(F_PERMISSION_OWNERLIST);
		List<User> result = new ArrayList<User>();
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
			for (Object userId : list) {
				User user = UserToolkit.getUserById((String) userId);
				if (user != null && !result.contains(user)) {
					result.add(user);
				}
			}
		}
		return result;
	}

	public void addToPermissionOwnerList(String userId) {
		Object value = getValue(F_PERMISSION_OWNERLIST);
		if (!(value instanceof BasicBSONList)) {
			value = new BasicDBList();
		}
		BasicBSONList listValue = (BasicBSONList) value;
		if (!listValue.contains(userId)) {
			listValue.add(userId);
			setValue(F_PERMISSION_OWNERLIST, listValue);
		}
	}

	public boolean isPermissionOwner(String userId) {
		Assert.isNotNull(userId);
		Object value = getValue(F_PERMISSION_OWNERLIST);
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
			for (Object _userId : list) {
				if (userId.equals(_userId)) {
					return true;
				}
			}
		}
		return false;
	}
}
