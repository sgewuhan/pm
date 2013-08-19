package com.sg.business.model.numbering;


import com.mobnut.db.DBActivator;
import com.mobnut.db.utils.DBUtil;
import com.mobnut.db.value.ExtendValue;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;

public class ProjectNumber extends ExtendValue {


	@Override
	public Object getExtendValue(Object po) {
		DBCollection ids = DBActivator.getCollection(IModelConstants.DB, IModelConstants.C__IDS);
		int id = DBUtil.getIncreasedID(ids, IModelConstants.SEQ_PROJECT_NUMBER);
		return String.format("%06x", id).toUpperCase();
	}

	
}
