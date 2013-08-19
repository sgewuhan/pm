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
 * <p>
 * 获得当前用户的可以管理的组织
 * <p/>
 * <br/>
 * 继承于 {@link com.mobnut.db.model.DataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class OrgOfBusinessAdminRoleUser extends DataSetFactory {
	
	/**
	 * 当前用户可访问的组织集合数
	 */
	private long count;

	/**
	 * 获取当前用户可访问的组织
	 * @param ds : 组织数据集
	 * @return 实例化的{@link com.sg.business.model.Organization}集合
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		//获得用户具有业务管理员(组织角色)的组织
		List<PrimaryObject> orgs = currentUser.getRoleGrantedOrganization(Role.ROLE_BUSINESS_ADMIN_ID);

		//获取当前用户可访问的组织集合数
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
