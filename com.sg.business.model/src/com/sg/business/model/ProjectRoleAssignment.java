package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;

/**
 * ��Ŀ��ɫָ��
 * @author jinxitao
 *
 */
public class ProjectRoleAssignment extends AbstractRoleAssignment implements IProjectRelative{

	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return "��Ŀָ��";
	}
	
	/**
	 * ������Ŀ
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
