package com.sg.business.model.dataset.projecttemplate;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 工作定义集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}，
 * 用于获得项目模版的工作定义信息，应用于工作前后置关系中选择项目模版的工作定义<br/>
 * 包括以下功能：
 * <li>获取项目模版的工作定义集合
 * <li>设置当前数据为当前项目模版所属的工作定义信息
 * 
 * @author yangjun
 *
 */
public class WorkDefConnectionOfProjecttemplateOption extends
		OptionDataSetFactory {

	/**
	 * 工作定义集合的构造函数,设置工作定义存在数据库及数据存储表
	 */
	public WorkDefConnectionOfProjecttemplateOption() {
		//设置工作定义存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
	}

	/**
	 * 设置当前数据为当前项目模版所属的工作定义信息
	 * @param data : 当前编辑数据
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//获取当前所属的项目模版信息
		Object projectTemplateId = data
				.getValue(WorkDefinition.F_PROJECT_TEMPLATE_ID);
		//设置查询条件为当前项目模版所属的工作定义信息
		setQueryCondition(new BasicDBObject().append(
				WorkDefinition.F_PROJECT_TEMPLATE_ID,
				projectTemplateId));
	}

}
