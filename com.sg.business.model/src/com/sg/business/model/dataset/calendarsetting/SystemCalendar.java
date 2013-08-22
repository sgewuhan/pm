package com.sg.business.model.dataset.calendarsetting;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;

public class SystemCalendar extends SingleDBCollectionDataSetFactory {

	public SystemCalendar() {
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
	}

	@Override
	public DBObject getSort() {
		return new SEQSorter().getBSON();
	}

}
