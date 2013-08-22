package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Work extends AbstractWork implements IProjectRelative {

	public static final String EDITOR_PROJECT_WORK = "editor.workDefinition";

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

	@Override
	public Work makeChildWork() {
		DBObject data = new BasicDBObject();
		data.put(WorkDefinition.F_PARENT_ID, get_id());
		data.put(WorkDefinition.F_ROOT_ID, getValue(F_ROOT_ID));

		int seq = getMaxChildSeq();
		data.put(F_SEQ, new Integer(seq + 1));

		data.put(F_PROJECT_ID, getValue(F_PROJECT_ID));

		Work po = ModelService.createModelObject(data, Work.class);
		return po;
	}
}
