package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 项目发起部门集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获得项目发起部门的集合信息，应用于新建项目中选择项目发起部门<br/>
 * 包括以下功能：
 * <li>获取项目发起部门集合
 * <li>获取所选项目项目经理所属组织的下级组织
 * <li>获取所选项目项目经理所属组织的上级组织
 * 
 * @author yangjun
 * 
 */
public class LaunchOrgOfPM extends MasterDetailDataSetFactory {

	/**
	 * 项目发起部门集合的构造函数,设置项目经理所在的项目管理部门存在数据库及数据存储表
	 */
	public LaunchOrgOfPM() {
		//设置项目经理所在的项目管理部门存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * 获取项目发起部门集合
	 * @return 项目经理所在的项目发起部门集合的数据集
	 */
	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		if (master != null) {
			//获取当前所选项目
			Project project = (Project) master;
			// 获得当前所选项目的项目经理
			User charger = project.getCharger();
			// 获得当前所选项目的项目经理所属的组织
			Organization org = charger.getOrganization();
			searchUp(org, list);
			searchDown(org, list);
		}
		return new DataSet(list);
	}

	/**
	 * 获取所选项目项目经理所属组织的下级组织，
	 * 将当前组织的其下属组织循环递归添加到List<{@link PrimaryObject}>
	 * @param org : 当前组织
	 * @param list ： 项目发起部门集合
	 */
	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			searchDown(child, list);
		}
	}

	/**
	 * 获取所选项目项目经理所属组织的上级组织，
	 * 将当前组织及其上级组织循环递归添加到List<{@link PrimaryObject}>
	 * 
	 * @param org ： 当前组织
	 * @param list ： 项目发起部门集合
	 */
	private void searchUp(Organization org, List<PrimaryObject> list) {
		list.add(0, org);
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	/**
	 * 设置项目发起部门集合的关联字段：{@link NULL}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return null;
	}
}
