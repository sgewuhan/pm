package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;


public class Work extends AbstractWork implements IProjectRelative{


	/**
	 * 必需的，不可删除，布尔类型的字段
	 */
	public static final String F_MANDATORY = "mandatory";

	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}
}
