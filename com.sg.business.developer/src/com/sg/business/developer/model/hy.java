package com.sg.business.developer.model;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class hy extends PrimaryObject {
	public static final String F_PARENT_ID = "parent_id";
	public hy() {
		// TODO Auto-generated constructor stub
	}
	public hy makeChildDemo1(hy po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), hy.class);
		}
		po.setValue(hy.F_PARENT_ID, get_id());
		return po;		
	}
	public hy1 makeDemo2(hy1 po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), hy1.class);
		}
		po.setValue(hy1.F_DEMO1_ID, get_id());
		return po;			
	}

}
