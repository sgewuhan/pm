package com.sg.business.model.dataset.organization;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.Role;
import com.sg.business.model.User;

/**
 * 获得当前用户的可以管理的组织
 * 
 * @author Administrator
 * 
 */
public class OrgOfBusinessAdminRoleUser extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		List<PrimaryObject> orgs = currentUser.getRoleGrantedOrganization(Role.ROLE_BUSINESS_ADMIN_ID);

		count = orgs.size();
		return orgs;
	}


	/**
	 * 用于翻页查询时预测全部的页数
	 */
	@Override
	public long getTotalCount() {
		return count;
	}

}
