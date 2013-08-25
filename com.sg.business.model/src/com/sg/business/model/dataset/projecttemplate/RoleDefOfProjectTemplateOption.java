package com.sg.business.model.dataset.projecttemplate;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 工作定义的负责人,参与者和指派者的角色集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}，
 * 用于获得工作定义的负责人,参与者和指派者的角色信息，应用于工作定义中选择负责人,参与者和指派者角色<br/>
 * 包括以下功能：
 * <li>获取工作定义的负责人,参与者和指派者的角色集合
 * <li>设置当前数据为当前项目模版所属的角色信息
 * 
 * @author yangjun
 *
 */
public class RoleDefOfProjectTemplateOption extends OptionDataSetFactory {

	/**
	 * 工作定义的负责人,参与者和指派者的角色集合的构造函数,设置角色存在数据库及数据存储表
	 */
	public RoleDefOfProjectTemplateOption() {
		//设置角色存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
	}

	/**
	 * 设置当前数据为当前项目模版所属的角色信息
	 * @param data : 当前编辑数据
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//获取当前工作定义所属的项目模版信息
		WorkDefinition workd = (WorkDefinition) data;
		ProjectTemplate projectTemplate = workd.getProjectTemplate();
		//设置查询条件为当前项目模版所属的角色信息
		setQueryCondition(new BasicDBObject().append(
				RoleDefinition.F_PROJECT_TEMPLATE_ID, projectTemplate.get_id()));
	}

}
