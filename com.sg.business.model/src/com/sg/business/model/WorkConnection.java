package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;

/**
 * 工作的前后置关系
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkConnection extends AbstractWorkConnection implements IProjectRelative {
	public static final String EDITOR = "editor.workConnection";

	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "工作前后置关系";
	}
	
	/**
	 * 返回所属项目
	 * @return Project
	 */
	@Override
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}
}
