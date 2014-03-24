package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.commonlabel.ProjectMonthDataCommonHTMLLable;
import com.sg.business.model.etl.IProjectETL;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class ProjectMonthData extends PrimaryObject implements IProjectETL{


	public Integer getYear() {
		return (Integer) getValue(F_YEAR);
	}

	public Integer getMonth() {
		return (Integer) getValue(F_MONTH);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (adapter == CommonHTMLLabel.class) {
			return (T) new ProjectMonthDataCommonHTMLLable(this);
		}
		return super.getAdapter(adapter);
	}
}
