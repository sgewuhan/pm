package com.sg.business.model;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

public class TaskForm extends PrimaryObject {

	public static final String F_WORK_ID = "workid";
	public static final String F_EDITOR = "form_editor";

	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}
	
	@Override
	public void doInsert(IContext context) throws Exception {
	}
	
	@Override
	public void doUpdate(IContext context) throws Exception {
	}

	public Work getWork() {
		ObjectId workid = (ObjectId) getValue(F_WORK_ID);
		return ModelService.createModelObject(Work.class, workid);
	}
}
