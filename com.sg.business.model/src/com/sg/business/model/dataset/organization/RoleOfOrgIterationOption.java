package com.sg.business.model.dataset.organization;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 工作定义的角色集合
 * </p>
 * 用于通用工作定义和独立工作定义 这两类工作定义直接使用了组织的角色，并不创建角色定义 获取选项时，
 * 需要返回该职能部门及下级的所有角色 这个类不保证线程安全
 * <br/>
 * 继承于{@link com.sg.widgets.commons.dataset.OptionDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class RoleOfOrgIterationOption extends OptionDataSetFactory {

	private Organization organization;

	/**
	 * 工作定义的角色集合构造函数
	 */
	public RoleOfOrgIterationOption() {
		//设置角色集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	/**
	 * 设置当前工作定义的组织
	 * @param data : 当前工作定义
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		// 获取当前工作定义
		WorkDefinition workd = (WorkDefinition) data;
		// 获得当前组织
		organization = workd.getOrganization();
	}

	/**
	 * 获取当前组织及下级组织的角色集合
	 * @return 当前组织及下级组织的角色集合的数据集
	 */
	@Override
	public DataSet getDataSet() {
		// 获取本级以及下级所有的角色
		List<PrimaryObject> dataItems = organization.getRolesIteration();
		return new DataSet(dataItems);
	}



}
