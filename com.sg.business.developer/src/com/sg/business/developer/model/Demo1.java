package com.sg.business.developer.model;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class Demo1 extends PrimaryObject{

	public static final String F_PARENT_ID = "parent_id";

	public Demo1 makeChildDemo1(Demo1 po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), Demo1.class);
		}
		po.setValue(Demo1.F_PARENT_ID, get_id());
		return po;		
	}

	public Demo2 makeDemo2(Demo2 po) {
		if(po == null){
			po = ModelService.createModelObject(new BasicDBObject(), Demo2.class);
		}
		po.setValue(Demo2.F_DEMO1_ID, get_id());
		return po;			
	}

}
