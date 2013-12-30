package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.etl.IProjectETL;

public class ProjectMonthData extends PrimaryObject implements IProjectETL{


	public Integer getYear() {
		return (Integer) getValue(F_YEAR);
	}

	public Integer getMonth() {
		return (Integer) getValue(F_MONTH);
	}

}
