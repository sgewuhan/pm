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
 * <p>
 * 组织文档库
 * </p>
 * 继承于 {@link com.mobnut.db.model.DataSetFactory}，获得当前用户所属组织的文档库
 * 包括：所属组织及其下级组织的文件夹
 * <br/>
 * 实现以下几种功能：
 * <li>获取所属组织及其下级组织的文件夹数
 * <li>获取所属组织及其下级组织的文件夹的List<{@link com.mobnut.db.model.PrimaryObject}>集合
 * 
 * @author yangjun
 * 
 */
public class VaultOfOrganization extends DataSetFactory {

	/**
	 * 当前用户所属组织及其下级组织的文件夹数
	 */
	private long count;

	/**
	 * 获取所属组织及其下级组织的文件夹的List<{@link com.mobnut.db.model.PrimaryObject}>集合
	 * @param ds : 组织容器数据集
	 * @return 实例化的List<{@link com.mobnut.db.model.PrimaryObject}>集合
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// 从当前的进程中获得登录用户的信息
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		// 在当前用户所在的组织以及下级组织中获取"是容器"的组织
		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();
		
		// 添加下级的组织集合
		addSubOrganizationContainer(org, containers);

		//获取当前用户可访问的组织容器集合数
		count = containers.size();
		return containers;
	}


	/**
	 * 递归添加所属组织的其下级组织的文件夹到List<{@link com.mobnut.db.model.PrimaryObject}>集合
	 * 
	 * @param startOrganization : 起始组织
	 * @param containers ： 组织容器集合
	 */
	private void addSubOrganizationContainer(Organization startOrganization,
			List<PrimaryObject> containers) {
		//判断起始组织是否为容器的组织，如果为容器的组织，则添加到OrganizationList中
		if (Boolean.TRUE.equals(startOrganization.isContainer())) {
			Container container = Container.adapter(startOrganization,
					Container.TYPE_OWNER);
			if(!containers.contains(container)){
				containers.add(container);
			}
		}
		//获取起始组织的下级组织，递归调用，将是容器的组织添加到OrganizationList中
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
