package com.sg.business.performence.model;

import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;

public class WorkWorksNode extends WorksNode{

	public WorkWorksNode(WorksNode parent, PrimaryObject data) {
		super(parent, data);
	}

	@Override
	public String getAdditionInfomation() {
		PrimaryObject work = getData();
		Date actualStart = (Date) work.getValue(Work.F_ACTUAL_START);
		Date actualFinish = (Date) work.getValue(Work.F_ACTUAL_FINISH);
		return String.format(Utils.FORMATE_DATE_SIMPLE, actualStart) + " ~ "
				+ String.format(Utils.FORMATE_DATE_SIMPLE, actualFinish);
	}
	
	

}
