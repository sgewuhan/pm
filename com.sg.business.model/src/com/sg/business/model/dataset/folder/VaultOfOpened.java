package com.sg.business.model.dataset.folder;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Container;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class VaultOfOpened extends DataSetFactory {
	private User currentUser;

	public VaultOfOpened() {
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		currentUser = UserToolkit.getUserById(userId);
	}

	public VaultOfOpened(String userId) {
		currentUser = UserToolkit.getUserById(userId);
	}

	/**
	 * 当前用户所属组织及其下级组织的文件夹数
	 */
	private long count;

	/**
	 * 获取所属组织及的上级组织的文件夹的List<{@link com.mobnut.db.model.PrimaryObject}>集合
	 * 
	 * @param ds
	 *            : 组织容器数据集
	 * @return 实例化的List<{@link com.mobnut.db.model.PrimaryObject}>集合
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		// 在当前用户所在的组织以及下级组织中获取"是容器"的组织
		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();

		addParentOrganizationContainer(org, containers);

		// 获取当前用户可访问的组织容器集合数
		count = containers.size();
		return containers;
	}

	private void addParentOrganizationContainer(Organization startOrganization,
			List<PrimaryObject> containers) {
		Organization parentOrg = (Organization) startOrganization
				.getParentOrganization();
		if (parentOrg != null) {
			addParentOrganizationContainer(parentOrg, containers);

			// 判断起始组织的上级组织中否为容器的组织，如果为容器的组织，则检查其是否存在开放属性的文件夹，如果存在则添加到OrganizationList中
			if (Boolean.TRUE.equals(parentOrg.isContainer())) {
				Container container = Container.adapter(parentOrg,
						Container.TYPE_OWNER);
				long count = parentOrg.getOpenedFolderCount();
				if (count > 0) {
					if (!containers.contains(container)) {
						containers.add(container);
					}
				}
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
