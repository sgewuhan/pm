package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public class DummyModel extends PrimaryObject {

	@Override
	public void doInsert(IContext context) throws Exception {
	}
	
	@Override
	public void doRemove(IContext context) throws Exception {
	}
	
	@Override
	public boolean doSave(IContext context) throws Exception {
		return true;
	}
	
}
