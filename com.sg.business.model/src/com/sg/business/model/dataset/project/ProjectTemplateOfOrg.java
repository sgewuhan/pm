package com.sg.business.model.dataset.project;

import org.bson.types.ObjectId;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * 项目模版集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}，
 * 用于获得项目模版的集合信息，应用于新建项目中选择项目模版<br/>
 * 包括以下功能：
 * <li>获取项目模版集合
 * <li>设置当前数据为所属组织或当前组织的项目模版
 * 
 * @author yangjun
 *
 */
public class ProjectTemplateOfOrg extends OptionDataSetFactory {

	/**
	 * 项目模版集合的构造函数,设置项目模版存在数据库及数据存储表
	 */
	public ProjectTemplateOfOrg() {
		//设置项目模版存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	/**
	 * 设置当前数据为所属组织或当前组织的项目模版
	 * @param data ：当前编辑数据
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		//获取当前项目所属的项目职能组织
		Project project = (Project) data;
		ObjectId org_id = project.getFunctionOrganizationId();
		//设置查询条件为所属组织或当前组织
		if(org_id!=null){
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F_ORGANIZATION_ID, org_id).append(ProjectTemplate.F_ACTIVATED, Boolean.TRUE));
		}else{
			setQueryCondition(new BasicDBObject().append(ProjectTemplate.F__ID, null));
		}
		super.setEditorData(data);
	}


}
