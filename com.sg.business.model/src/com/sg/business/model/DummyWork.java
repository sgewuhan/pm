package com.sg.business.model;

import com.mobnut.db.model.IContext;
import com.mongodb.DBObject;

public class DummyWork extends Work {

	private Work work;
	public static String WORK_ID = "workid";

	public void setSource(Work work) {
		this.work = work;
		DBObject data = work.get_data();
		data.put(WORK_ID, work.get_id());
		data.removeField(F__ID);
		set_data(data);
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		DBObject data = get_data();
		Object workid = data.get(WORK_ID);
		data.put(F__ID, workid);
		data.removeField(WORK_ID);
		work.set_data(data);
		return work.doSave(context);
	}

	@Override
	public boolean canEdit(IContext context) {
		return true;
	}

}
