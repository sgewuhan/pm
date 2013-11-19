package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 项目工作集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获得项目工作的集合信息，应用于新建项目的WBS显示<br/>
 * 包括以下功能：
 * <li>获取项目工作集合数据信息
 * <li>通过F__ID关联所选项目
 * <li>设置项目的关联字段F_WORK_ID
 * 
 * @author yangjun
 *
 */
public class WorkOfProject extends MasterDetailDataSetFactory {

	/**
	 * 项目工作的集合构造函数,用于设置项目工作的集合的存放数据库及数据存储表
	 */
	public WorkOfProject() {
		//设置项目工作的集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK);
		//设置项目工作集合的排序方式，使用默认的seq字段进行排序
		setSort(new SEQSorter().getBSON());
	}

	/**
	 * 设置项目工作集合与项目的关联字段：{@link com.sg.business.model.Work.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Work.F__ID;
	}


	/**
	 * 改变获取项目的引用值：{@link com.sg.business.model.VisProject.F_WORK_ID }
	 */
	@Override
	protected Object getMasterValue() {
		return master.getValue(Project.F_WORK_ID);
	}
	
	
}
