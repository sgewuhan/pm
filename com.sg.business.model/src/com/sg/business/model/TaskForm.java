package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public class TaskForm extends PrimaryObject {

	public static final String F_WORK_ID = "workid";

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
}
