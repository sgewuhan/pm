package com.sg.business.model.dataset.project;

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
 * 项目管理部门集合
 * </p>
 * 继承于{@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory},
 * 用于获得项目管理部门集合信息，应用于新建项目中选择项目管理部门<br/>
 * 包括以下功能：
 * <li>获取项目管理部门集合
 * <li>获取所选项目项目经理所属组织的下级具有项目管理职能的组织
 * <li>获取所选项目项目经理所属组织的上级具有项目管理职能的组织
 * 
 * @author yangjun
 * 
 */
public class PMOrgOfPM extends MasterDetailDataSetFactory {

	/**
	 * 项目管理部门集合的构造函数,设置项目经理所在的项目管理部门存在数据库及数据存储表
	 */
	public PMOrgOfPM() {
		//
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * 获取项目管理部门集合
	 * @return 项目经理所在的项目管理部门集合的数据集
	 */
	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		if (master != null) {
			Project project = (Project) master;
			// 获得项目经理
			User charger = project.getCharger();
			// 获得项目经理所属的组织
			Organization org = charger.getOrganization();
			searchUp(org, list);
			searchDown(org, list);
		}
		return new DataSet(list);
	}

	/**
	 * 获取所选项目项目经理所属组织的下级具有项目管理职能的组织，
	 * 将当前组织的其下属具有项目管理职能的组织循环递归添加到List<{@link PrimaryObject}>
	 * 
	 * @param org : 当前组织
	 * @param list ： 项目管理部门集合
	 */
	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			//判断当前组织是否是具有项目管理职能的组织，如果“是”则添加到对应的List<PrimaryObject>中
			if (child.isFunctionDepartment()) {
				list.add(child);
			}
			searchDown(child, list);
		}
	}

	/**
	 * 获取所选项目项目经理所属组织的上级具有项目管理职能的组织，
	 * 将当前组织及其上级具有项目管理职能的组织循环递归添加到List<{@link PrimaryObject}>
	 * 
	 * @param org ： 当前组织
	 * @param list ： 项目管理部门集合
	 */
	private void searchUp(Organization org, List<PrimaryObject> list) {
		//判断当前组织是否是具有项目管理职能的组织，如果“是”则添加到对应的List<PrimaryObject>中
		if (org.isFunctionDepartment()) {
			list.add(0, org);
		}
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	/**
	 * 设置项目管理部门集合的关联字段：{@link NULL}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return null;
	}
}
