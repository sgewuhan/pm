package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.sg.business.resource.nls.Messages;

/**
 * ������ǰ���ù�ϵ
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkConnection extends AbstractWorkConnection implements IProjectRelative {
	public static final String EDITOR = "editor.workConnection"; //$NON-NLS-1$

	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().WorkConnection_1;
	}
	
	/**
	 * ����������Ŀ
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
