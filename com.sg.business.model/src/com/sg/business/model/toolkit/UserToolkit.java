package com.sg.business.model.toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;

public class UserToolkit {
	private static ConcurrentHashMap<String, User> USERMAP = new ConcurrentHashMap<String, User>();

	/**
	 * 获取系统管理员
	 * 
	 * @return List<User>
	 */
	public static List<PrimaryObject> getAdmin() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);

		DBCursor cur = userCol.find(new BasicDBObject().append(User.F_IS_ADMIN,
				Boolean.TRUE));
		while (cur.hasNext()) {
			result.add(ModelService.createModelObject(cur.next(), User.class));
		}
		return result;
	}

	/**
	 * 根据ID获取用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public static User getUserById(String userId) {
		if (Utils.isNullOrEmpty(userId)) {
			return null;
		}
		User user = USERMAP.get(userId);
		if (user == null) {
			DBCollection userCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_USER);
			DBObject userData = userCol.findOne(new BasicDBObject().append(
					User.F_USER_ID, userId));
			user = ModelService.createModelObject(userData, User.class);
			if (user != null) {
				USERMAP.put(userId, user);
			}
		}
		return user;
	}

	public static void updateUser(User user) {
		if (user == null) {
			return;
		}
		String userId = user.getUserid();
		if (Utils.isNullOrEmpty(userId)) {
			return;
		}
		USERMAP.put(userId, user);
	}

	public static void updateUser(String userId) {
		if (userId == null) {
			return;
		}
		USERMAP.remove(userId);
		getUserById(userId);
	}

	public static void updateUser(ObjectId _Id) {
		if (_Id == null) {
			return;
		}
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		DBObject userData = userCol.findOne(new BasicDBObject().append(
				User.F__ID, _Id));
		User user = ModelService.createModelObject(userData, User.class);
		if (user != null) {
			String userId = user.getUserid();
			USERMAP.remove(userId);
			USERMAP.put(userId, user);
		}
	}

	public static void updateUser(ObjectId[] userIdList) {
		for (ObjectId userId : userIdList) {
			updateUser(userId);
		}
	}
}
