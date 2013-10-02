package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * 当前用户的上级组织和下级组织
 * </p>
 * 
 */
public class LaunchOrgOfCurrentUser extends SingleDBCollectionDataSetFactory {

	public LaunchOrgOfCurrentUser() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * 获取项目发起部门集合
	 * 
	 * @return 项目经理所在的项目发起部门集合的数据集
	 */
	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		User user = UserToolkit.getUserById(userId);
		
		Organization org = user.getOrganization();
		searchUp(org, list);
		searchDown(org, list);
		return new DataSet(list);
	}

	/**
	 * 获取所选项目项目经理所属组织的下级组织， 将当前组织的其下属组织循环递归添加到List<{@link PrimaryObject}>
	 * 
	 * @param org
	 *            : 当前组织
	 * @param list
	 *            ： 项目发起部门集合
	 */
	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			searchDown(child, list);
		}
	}

	/**
	 * 获取所选项目项目经理所属组织的上级组织， 将当前组织及其上级组织循环递归添加到List<{@link PrimaryObject}>
	 * 
	 * @param org
	 *            ： 当前组织
	 * @param list
	 *            ： 项目发起部门集合
	 */
	private void searchUp(Organization org, List<PrimaryObject> list) {
		list.add(0, org);
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}
}
