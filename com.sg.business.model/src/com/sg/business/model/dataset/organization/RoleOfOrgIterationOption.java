package com.sg.business.model.dataset.organization;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * 用于通用工作定义和独立工作定义 这两类工作定义直接使用了组织的角色，并不创建角色定义 获取选项时，需要返回该职能部门及下级的所有角色 这个类不保证线程安全
 * 
 * @author Administrator
 * 
 */
public class RoleOfOrgIterationOption extends OptionDataSetFactory {

	private Organization organization;

	public RoleOfOrgIterationOption() {
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		WorkDefinition workd = (WorkDefinition) data;
		// 获得职能部门
		organization = workd.getOrganization();
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems = organization.getRolesIteration();
		return new DataSet(dataItems);
	}



}
