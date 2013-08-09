package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

public class User extends PrimaryObject {

	public static final String F_EMAIL = "email";

	public static final String F_USER_ID = "userid";

	public static final String F_USER_NAME = "username";

	public static final String F_NICK = "nick";

	public static final String F_HEADPIC = "headpic";

	public static final String F_ORGANIZATION_ID = "organization_id";

	public static final String F_ORGANIZATION_NAME = "organization_name";

	public ObjectId getOrganization_id() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	public String getUserid() {
		return (String) getValue(F_USER_ID);
	}

	public String getUsername() {
		return (String) getValue(F_USER_NAME);
	}

	/**
	 * 获得用户从属的组织
	 * 
	 * @return
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
	 * 获取用户授予的角色
	 * 
	 * @param roleNumber
	 *            , 如果传空,查询所有的角色
	 * @return
	 */
	public List<PrimaryObject> getRoles(String roleNumber) {

		// 获得角色指派集合
		DBCollection roleAssignmentCol = DBActivator.getCollection(
				IModelConstants.DB, IModelConstants.C_ROLE_ASSIGNMENT);

		// 构造查询条件
		BasicDBObject condition = new BasicDBObject();
		condition.put(RoleAssignment.F_USER_ID, getUserid());

		// 同样一个角色的编号可能被应用到多个组织中，所以可能多个角色对象的角色编号可能相同
		// 如果传递了roleNumber，需要查询符合该角色编号的所有角色
		if (roleNumber != null) {
			condition.put(RoleAssignment.F_ROLE_NUMBER, roleNumber);
		}

		// 执行查询
		// 由于只需要获得角色的id,所以限定了查询的返回字段
		DBCursor cur = roleAssignmentCol.find(condition,
				new BasicDBObject().append(RoleAssignment.F_ROLE_ID, 1));

		// 取出角色id
		ObjectId[] roleIds = new ObjectId[cur.size()];
		int i = 0;
		while (cur.hasNext()) {
			roleIds[i++] = (ObjectId) cur.next().get(RoleAssignment.F_ROLE_ID);
		}

		List<PrimaryObject> result = new ArrayList<PrimaryObject>();

		// 获得角色集合
		DBCollection roleCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ROLE);

		// 查询id包含在已经取出的id数组中的记录，并反射为角色对象
		cur = roleCol.find(new BasicDBObject().append(Role.F__ID,
				new BasicDBObject().append("$in", roleIds)));
		while (cur.hasNext()) {
			result.add(ModelService.createModelObject(cur.next(), Role.class));
		}

		return result;
	}

	@Override
	public String getLabel() {
		return getUsername() + "|" + getUserid();
	}

	@Override
	public Image getImage() {
		if (getValue(F_ORGANIZATION_ID) == null) {
			return BusinessResource.getImage(BusinessResource.IMAGE_USER2_16);
		} else {
			return BusinessResource.getImage(BusinessResource.IMAGE_USER_16);
		}
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		if (context!=null&&"organization.member".equals(context.getPartId())) {// 删除团队成员

			setValue(F_ORGANIZATION_ID, null);
			try {
				doSave(context);
			} catch (Exception e) {
			}
		} else {
			super.doRemove(context);
		}
	}

	public static User getUserById(String userId) {
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_USER);
		DBObject userData = userCol.findOne(new BasicDBObject().append(User.F_USER_ID, userId));
		return ModelService.createModelObject(userData, User.class);
	}

}
