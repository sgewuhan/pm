package com.sg.business.model.dataset.calendarsetting;

import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.bson.SEQSorter;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectCalendar extends MasterDetailDataSetFactory {

	public ProjectCalendar() {
		super(IModelConstants.DB, IModelConstants.C_CALENDAR_SETTING);
		setSort(new SEQSorter().getBSON());
	}

	@Override
	protected String getDetailCollectionKey() {
		return CalendarSetting.F_PROJECT_ID;
	}


}
