package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 工作定义集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选项目模版的工作定义集合信息<br/>
 * 实现以下几种功能：
 * <li>获取工作定义集合数据信息
 * <li>通过id关联所选项目模版
 * 
 * @author yangjun
 *
 */
public class GenericWorkDef extends MasterDetailDataSetFactory {

	/**
	 * 获取工作定义集合数据信息,用于设置工作定义集合的存放数据库及数据存储表
	 */
	public GenericWorkDef() {
		//设置工作定义集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
	}

	/**
	 * 设置工作定义集合与项目模版的关联字段：{@link com.sg.business.model.WorkDefinition.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F__ID;
	}

}
