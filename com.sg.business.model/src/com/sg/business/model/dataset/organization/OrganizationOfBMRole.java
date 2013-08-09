package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;

/**
 * 获得当前用户的可以管理的组织
 * 
 * @author Administrator
 * 
 */
public class OrganizationOfBMRole extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		List<PrimaryObject> orgs = new ArrayList<PrimaryObject>();
		
		// 添加下级的可管理的组织
		addRoleGrantedOrganization(currentUser,
				Role.ROLE_BUSINESS_ADMIN_ID, orgs);
		
		count = orgs.size();
		return orgs;
	}

	private void addRoleGrantedOrganization(User user,
			String roleNumber,  List<PrimaryObject> orgs) {
		// 获得用户被授予的roleNumber对应的角色
		List<PrimaryObject> roles = user.getRoles(roleNumber);

		// 取出这些角色的所属组织的id
		ObjectId[] orgIds = new ObjectId[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			orgIds[i] = ((Role) roles.get(i)).getOrganization_id();
		}

		// 查询属于可管理的组织
		DBCollection orgCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject condition = new BasicDBObject();
		condition.put(Organization.F__ID,
				new BasicDBObject().append("$in", orgIds));
		condition.put(Organization.F_IS_FUNCTION_DEPARTMENT, Boolean.TRUE);
		DBCursor cur = orgCol.find(condition);
		while (cur.hasNext()) {
			Organization org = ModelService.createModelObject(cur.next(),
					Organization.class);
			if(!orgs.contains(org)){
				orgs.add(org);
			}
		}
	}


	/**
	 * 用于翻页查询时预测全部的页数
	 */
	@Override
	public long getTotalCount() {
		return count;
	}

}
