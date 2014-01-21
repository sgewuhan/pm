package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.sg.business.resource.nls.Messages;

/**
 * 工作的前后置关系
 * <p/>
 * 
 * @author zhonghua
 * 
 */
public class WorkConnection extends AbstractWorkConnection implements IProjectRelative {
	public static final String EDITOR = "editor.workConnection"; //$NON-NLS-1$

	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().WorkConnection_1;
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
