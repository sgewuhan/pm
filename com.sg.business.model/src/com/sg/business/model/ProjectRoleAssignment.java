package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;

public class ProjectRoleAssignment extends AbstractRoleAssignment implements IProjectRelative{

	
	@Override
	public String getTypeName() {
		return "ÏîÄ¿Ö¸ÅÉ";
	}
	
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
