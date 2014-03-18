package com.sg.business.model.dataset.organization;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class UserAdminDataSetFactory extends SingleDBCollectionDataSetFactory {

	private String userId;

	public UserAdminDataSetFactory() {
		// 设置用户集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_USER);
		// 设置查询条件
		userId = new CurrentAccountContext().getUserId();

	}

	@Override
	public DBObject getQueryCondition() {
		User user = UserToolkit.getUserById(userId);
		if (user.isAdmin()) {
			return null;
		}
		List<PrimaryObject> roles = user
				.getRoles(Role.ROLE_ORGANIZATION_ADMIN_ID);
		if (roles == null || roles.isEmpty()) {
			return new BasicDBObject().append(User.F__ID, null);
		}

		Set<ObjectId> ids = new HashSet<ObjectId>();
		for (PrimaryObject primaryObject : roles) {
			Role role = (Role) primaryObject;
			Organization org = role.getOrganization();
			addOrg(org, ids);
		}

		
		BasicDBObject cd1 = new BasicDBObject().append(User.F_ORGANIZATION_ID,
				new BasicDBObject().append("$in", ids.toArray()));
		BasicDBObject cd2 = new BasicDBObject().append(User.F_ORGANIZATION_ID,
				null);
		return new BasicDBObject().append("$or", new BasicDBObject[]{cd1,cd2});
	}

	private void addOrg(Organization org, Set<ObjectId> ids) {
		ids.add(org.get_id());
		List<PrimaryObject> children = org.getChildrenOrganization();
		if (children != null && children.size() > 0) {
			for (PrimaryObject primaryObject : children) {
				Organization child = (Organization) primaryObject;
				addOrg(child, ids);
			}
		}
	}
}
