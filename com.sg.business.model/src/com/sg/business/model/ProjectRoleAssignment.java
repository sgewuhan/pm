package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;

/**
 * 项目角色指派
 * 
 * @author jinxitao
 * 
 */
public class ProjectRoleAssignment extends AbstractRoleAssignment implements
		IProjectRelative {

	/**
	 * 返回类型名称
	 * 
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "项目指派";
	}

	/**
	 * 返回项目
	 * 
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

	public ProjectRole getProjectRole() {
		ObjectId prId = (ObjectId) getValue(F_ROLE_ID);
		if (prId != null) {
			return ModelService.createModelObject(ProjectRole.class, prId);
		} else {
			return null;
		}
	}

}
