package com.sg.business.organization.command;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Message;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.organization.nls.Messages;
import com.sg.sqldb.utility.SQLResult;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;
import com.sg.widgets.part.BackgroundContext;

public class UserExchange {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * HR系统组织ID
	 */
	private String unitId;

	/**
	 * 邮箱
	 */
	private String eMail;

	/**
	 * PM系统组织ID
	 */
	private ObjectId pmUnitId;

	/**
	 * 消息标题
	 */
	public static final String MESSAGE_DESC = Messages.get().UserExchange_0;

	/**
	 * 消息内容（后半部分）
	 */
	public static final String MESSAGE_CONTENT_AFTER1 = Messages.get().UserExchange_1;

	/**
	 * 消息内容（后半部分）
	 */
	public static final String MESSAGE_CONTENT_AFTER2 = Messages.get().UserExchange_2;

	/**
	 * 消息内容（前半部分）
	 */
	public static final String MESSAGE_CONTENT_BEFORE = Messages.get().UserExchange_3;

	/**
	 * 删除
	 */
	public static final String MESSAGE_SENDTYPE_DELETE = Messages.get().UserExchange_4;

	/**
	 * 修改
	 */
	public static final String MESSAGE_SENDTYPE_UPDATE = Messages.get().UserExchange_5;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ObjectId getPmUnitId() {
		return pmUnitId;
	}

	public void setPmUnitId(ObjectId pmUnitId) {
		this.pmUnitId = pmUnitId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUnitId() {
		if (unitId == null) {
			Organization org = getPmOrgByOrganizationNumber();
			if (org != null) {
				unitId = org.getOrganizationNumber();
			}
		}
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	/**
	 * 获取PM系统用户
	 * 
	 * @param pmOrgId
	 *            : 用户id
	 * @return
	 */
	public UserExchange getByPmUser(String userId) {
		UserExchange userExchange = new UserExchange();
		// 获取用户信息
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		DBObject childRow = coll.findOne(new BasicDBObject().append(
				User.F_USER_ID, userId));
		// 构造用户
		userExchange.setUserId((String) childRow.get(User.F_USER_ID));
		userExchange.seteMail((String) childRow.get(User.F_EMAIL));
		userExchange.setUserName((String) childRow.get(User.F_USER_NAME));
		userExchange.setPmUnitId((ObjectId) childRow
				.get(User.F_ORGANIZATION_ID));
		return userExchange;
	}

	/**
	 * 获取PM系统用户集合
	 * 
	 * @param pmOrgId
	 *            : PM系统组织id
	 * @return
	 */
	public Set<UserExchange> getByPmUsers(ObjectId pmOrgId) {
		Set<UserExchange> childrenSet = new HashSet<UserExchange>();
		// 获取用户
		DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		DBCursor childCursor;
		if (pmOrgId != null) {
			childCursor = coll.find(new BasicDBObject().append(User.F__ID,
					pmOrgId));
		} else {
			childCursor = coll.find();
		}
		// 循环构造用户
		while (childCursor.hasNext()) {
			DBObject childRow = childCursor.next();
			UserExchange userExchange = new UserExchange();
			userExchange.setUserId((String) childRow.get(User.F_USER_ID));
			userExchange.seteMail((String) childRow.get(User.F_EMAIL));
			userExchange.setUserName((String) childRow.get(User.F_USER_NAME));
			userExchange.setPmUnitId((ObjectId) childRow
					.get(User.F_ORGANIZATION_ID));
			childrenSet.add(userExchange);
		}
		return childrenSet;
	}

	/**
	 * 获取HR系统用户
	 * 
	 * @param hrOrgId
	 * @return
	 */
	public Set<UserExchange> getBySqlUsers(String hrOrgId) {
		Set<UserExchange> childrenSet = new HashSet<UserExchange>();
		SQLResult result;
		SQLRow row;
		try {
			// 获取用户
			if (hrOrgId != null) {
				result = SQLUtil.SQL_QUERY("hr", //$NON-NLS-1$
						"select * from tb_nczz.pm_emp where unit = '" + hrOrgId + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				String query = ""; //$NON-NLS-1$
				DBCollection coll = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_ORGANIZATION);
				DBCursor childCursor = coll.find();
				// 循环构造当前组织的子组织
				while (childCursor.hasNext()) {
					DBObject childRow = childCursor.next();
					if (query == "") { //$NON-NLS-1$
						query = (String) childRow
								.get(Organization.F_ORGANIZATION_NUMBER);
					} else {
						query = query
								+ "','" //$NON-NLS-1$
								+ (String) childRow
										.get(Organization.F_ORGANIZATION_NUMBER);
					}

				}
				if (query != "") { //$NON-NLS-1$
					result = SQLUtil.SQL_QUERY("hr", //$NON-NLS-1$
							"select * from tb_nczz.pm_emp where unit in ('" //$NON-NLS-1$
									+ query + "')"); //$NON-NLS-1$
				} else {
					result = SQLUtil.SQL_QUERY("hr", //$NON-NLS-1$
							"select * from tb_nczz.pm_emp "); //$NON-NLS-1$
				}
			}
			if (!result.isEmpty()) {
				// 循环构造用户
				Iterator<SQLRow> iter = result.iterator();
				while (iter.hasNext()) {
					row = iter.next();
					UserExchange userExchange = new UserExchange();
					userExchange.seteMail("" + row.getValue("email")); //$NON-NLS-1$ //$NON-NLS-2$
					userExchange.setUnitId("" + row.getValue("unit")); //$NON-NLS-1$ //$NON-NLS-2$
					userExchange.setUserId("" + row.getValue("code")); //$NON-NLS-1$ //$NON-NLS-2$
					userExchange.setUserName("" + row.getValue("name")); //$NON-NLS-1$ //$NON-NLS-2$
					childrenSet.add(userExchange);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childrenSet;
	}

	/**
	 * 获取两个用户集合的差异部分
	 * 
	 * @param otherUser1
	 * @param otherUser2
	 * @return
	 */
	public Set<UserExchange> getDifferentUser(Set<UserExchange> otherUser1,
			Set<UserExchange> otherUser2) {
		Set<UserExchange> result = new HashSet<UserExchange>();
		result.addAll(otherUser1);
		result.removeAll(otherUser2);
		return result;
	}

	/**
	 * 获取两个用户集合的相同部分
	 * 
	 * @param otherUser1
	 * @param otherUser2
	 * @return
	 */
	public Set<UserExchange> getSameUser(Set<UserExchange> otherUser1,
			Set<UserExchange> otherUser2) {
		Set<UserExchange> result = new HashSet<UserExchange>();
		result.addAll(otherUser1);
		result.removeAll(getDifferentUser(otherUser1, otherUser2));
		return result;
	}

	/**
	 * 比较当前用户和传入的用户归属组织是否一致
	 * 
	 * @param otherUser
	 * @return
	 */
	public boolean getDifferentUnitId(UserExchange otherUser) {
		String unitId = getUnitId();
		String otherUserUnitId = otherUser.getUnitId();
		if (unitId != null && otherUserUnitId != null) {
			return !unitId.equals(otherUserUnitId);
		} else {
			return false;
		}
	}

	/**
	 * 发送需要系统管理员操作的用户消息
	 * 
	 * @param messageSet
	 * @param sendType
	 *            : 消息类型，常量{@link UserExchange.MESSAGE_SENDTYPE_UPDATE}和
	 *            {@link UserExchange.MESSAGE_SENDTYPE_DELETE}
	 */
	public void sendMessage(Set<UserExchange> messageSet, String sendType) {
		Message message;
		// 设置消息内容
		String messageContent = null;
		String orgInfo = ""; //$NON-NLS-1$
		if (UserExchange.MESSAGE_SENDTYPE_UPDATE.equals(sendType)) {
			orgInfo = Messages.get().UserExchange_6;
		}
		for (UserExchange userExchange : messageSet) {
			if (orgInfo != "") { //$NON-NLS-1$
				orgInfo = orgInfo
						+ userExchange.getPmOrgByOrganizationId().getDesc();
			}
			if (messageContent != null) {
				messageContent = messageContent + "<br/>" //$NON-NLS-1$
						+ UserExchange.MESSAGE_CONTENT_BEFORE
						+ userExchange.getUserName()
						+ UserExchange.MESSAGE_CONTENT_AFTER1 + sendType
						+ orgInfo + UserExchange.MESSAGE_CONTENT_AFTER2;
			} else {
				messageContent = UserExchange.MESSAGE_CONTENT_BEFORE
						+ userExchange.getUserName()
						+ UserExchange.MESSAGE_CONTENT_AFTER1 + sendType
						+ orgInfo + UserExchange.MESSAGE_CONTENT_AFTER2;
			}
		}

		// 获取系统管理员角色的用户信息
		List<PrimaryObject> user = UserToolkit.getAdmin();
		BasicBSONList recieverList = new BasicBSONList();
		for (int i = 0; i < user.size(); i++) {
			recieverList.add(user.get(i).getValue(User.F_USER_ID));
		}

		// 发送消息
		message = ModelService.createModelObject(Message.class);
		message.setValue(Message.F_CONTENT, messageContent);
		message.setValue(Message.F_DESC, UserExchange.MESSAGE_DESC + sendType);
		message.setValue(Message.F_ISHTMLBODY, Boolean.TRUE);
		message.setValue(Message.F_RECIEVER, recieverList);
		try {
			message.doSave(new BackgroundContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入用户信息
	 * 
	 * @param insertSet
	 */
	public void doAddHR(Set<UserExchange> insertSet) {
		User user;
		for (UserExchange userExchange : insertSet) {
			Organization org = userExchange.getPmOrgByOrganizationNumber();

			user = ModelService.createModelObject(User.class);
			user.setValue(User.F_USER_ID, userExchange.getUserId());
			user.setValue(User.F_EMAIL, userExchange.geteMail());
			user.setValue(User.F_USER_NAME, userExchange.getUserName());
			user.setValue(User.F_ACTIVATED, Boolean.TRUE);
			if (org != null) {
				user.setValue(User.F_ORGANIZATION_ID, org.get_id());
				user.setValue(User.F_ORGANIZATION_NAME, org.getDesc());
				user.setValue("_orgnumber", org.getOrganizationNumber()); //$NON-NLS-1$
			}
			try {
				user.doSave(new BackgroundContext());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 依据组织编号，获取当前用户所对应的组织
	 * 
	 * @return
	 */
	public Organization getPmOrgByOrganizationNumber() {
		DBObject org;
		if (unitId != null) {
			DBCollection coll = DBActivator.getCollection(IModelConstants.DB,
					IModelConstants.C_ORGANIZATION);
			org = coll.findOne(new BasicDBObject().append(
					Organization.F_ORGANIZATION_NUMBER, unitId));
			return ModelService.createModelObject(org, Organization.class);
		} else {
			if (pmUnitId != null) {
				return ModelService.createModelObject(Organization.class,
						pmUnitId);
			} else {
				return null;
			}
		}
	}

	/**
	 * 依据组织编号，获取当前用户所对应的组织
	 * 
	 * @return
	 */
	public Organization getPmOrgByOrganizationId() {
		return ModelService.createModelObject(Organization.class, pmUnitId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserExchange other = (UserExchange) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
}
