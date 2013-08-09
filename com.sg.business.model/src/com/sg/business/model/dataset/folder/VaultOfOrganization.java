package com.sg.business.model.dataset.folder;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.Container;
import com.sg.business.model.Organization;
import com.sg.business.model.User;

/**
 * 获得当前用户的可用目录 包括：所属组织的文件夹，负责的项目的文件夹
 * 
 * @author Administrator
 * 
 */
public class VaultOfOrganization extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		// 在当前用户所在的组织以及下级组织中获取"是容器"的组织
		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();
		
//		// 添加授予“文档管理员”角色的组织容器
//		addRoleGrantedOrganizationContainer(currentUser,
//				Role.ROLE_VAULT_ADMIN_ID,Container.TYPE_ADMIN_GRANTED, containers);
		
		// 添加下级的组织容器
		addSubOrganizationContainer(org, containers);
		
//		// 添加授予“文档访问者”角色的组织容器
//		addRoleGrantedOrganizationContainer(currentUser,
//				Role.ROLE_VAULT_GUEST_ID,Container.TYPE_GUEST_GRANTED, containers);

		count = containers.size();
		return containers;
	}


	/**
	 * 添加下级的组织容器 从起始组织以及该组织的下级组织，如果是容器的组织，添加到orgList中
	 * 
	 * @param startOrganization
	 *            起始组织
	 * @param containers
	 */
	private void addSubOrganizationContainer(Organization startOrganization,
			List<PrimaryObject> containers) {
		if (Boolean.TRUE.equals(startOrganization.isContainer())) {
			Container container = Container.adapter(startOrganization,
					Container.TYPE_OWNER);
			if(!containers.contains(container)){
				containers.add(container);
			}
		}
		List<PrimaryObject> children = startOrganization
				.getChildrenOrganization();
		for (PrimaryObject child : children) {
			addSubOrganizationContainer((Organization) child, containers);
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
