package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 交付物定义集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选项目模版的交付物定义集合信息<br/>
 * 实现以下几种功能：
 * <li>获取交付物定义集合数据信息
 * <li>通过F__ID关联所选项目模版
 * <li>设置项目模版的关联字段F_WORK_DEFINITON_ID
 * 
 * @author yangjun
 *
 */
public class WorkDefOfProjectTemplate extends MasterDetailDataSetFactory {

	/**
	 * 交付物定义集合构造函数,用于设置交付物定义集合的存放数据库及数据存储表
	 */
	public WorkDefOfProjectTemplate() {
		//设置交付物定义集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		//设置交付物定义集合的排序方式，使用默认的seq字段进行排序
		setSort(new SEQSorter().getBSON());
	}

	/**
	 * 设置交付物定义集合与项目模版的关联字段：{@link com.sg.business.model.WorkDefinition.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F__ID;
	}

	/**
	 * 改变获取项目模版的引用值：{@link com.sg.business.model.ProjectTemplate.F_WORK_DEFINITON_ID }
	 */
	@Override
	protected Object getMasterValue() {
		return master.getValue(ProjectTemplate.F_WORK_DEFINITON_ID);
	}
	
	
}
