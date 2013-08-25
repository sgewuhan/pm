package com.sg.business.model.dataset.project;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectRole;
import com.sg.business.model.Work;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 工作的负责人,参与者和指派者的角色集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}，
 * 用于获得工作的负责人,参与者和指派者的角色信息，应用于工作中选择负责人,参与者和指派者角色<br/>
 * 包括以下功能：
 * <li>获取工作的负责人,参与者和指派者的角色集合
 * <li>设置当前数据为当前项目所属的角色信息
 * 
 * @author yangjun
 *
 */
public class RoleDefOfProjectOption extends OptionDataSetFactory {

	/**
	 * 工作的负责人,参与者和指派者的角色集合的构造函数,设置项目角色存在数据库及数据存储表
	 */
	public RoleDefOfProjectOption() {
		//设置项目角色存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT_ROLE);
	}

	/**
	 * 设置当前数据为当前项目所属的角色信息
	 * @param data : 当前编辑数据
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//获取当前工作所属的项目信息
		Work workd = (Work) data;
		Project project = workd.getProject();
		//设置查询条件为当前项目所属的角色信息
		setQueryCondition(new BasicDBObject().append(
				ProjectRole.F_PROJECT_ID, project.get_id()));
	}

}
