package com.sg.business.model;

import java.util.Date;

import com.mobnut.db.model.PrimaryObject;

public class SummaryOrganizationWorks implements IWorksSummary {



	public SummaryOrganizationWorks(PrimaryObject po) {
	}

	@Override
	public double getWorksSummary(Date start, Date end) {
		return 0;
	}

	@Override
	public double getWorksSummaryOfDay(Date date) {
		double result = 0d;
		
		return result;
	}


}
