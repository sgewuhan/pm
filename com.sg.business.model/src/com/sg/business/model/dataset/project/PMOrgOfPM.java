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
 * 获得项目经理所在的项目管理部门
 * 
 * @author Administrator
 * 
 */
public class PMOrgOfPM extends MasterDetailDataSetFactory {

	public PMOrgOfPM() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

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

	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			if (child.isFunctionDepartment()) {
				list.add(child);
			}
			searchDown(child, list);
		}
	}

	private void searchUp(Organization org, List<PrimaryObject> list) {
		if (org.isFunctionDepartment()) {
			list.add(0, org);
		}
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}
}
