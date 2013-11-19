package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public abstract class ProjectProvider extends PrimaryObject {

	public abstract List<PrimaryObject> getProjectSet();
	
	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}
	
	@Override
	public void doUpdate(IContext context) throws Exception {
	}
	
	@Override
	public void doInsert(IContext context) throws Exception {
	}
	
	

}
