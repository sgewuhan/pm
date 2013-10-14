package com.sg.business.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;

public class SummaryOrganizationWorks implements IWorksSummary {

	private Organization organization;

	public SummaryOrganizationWorks(PrimaryObject po) {
		this.organization = (Organization) po;
	}

	@Override
	public double getWorksSummary(Date start, Date end) {
		return 0;
	}

	@Override
	public double getWorksSummaryOfDay(Date date, HashMap<ObjectId,PrimaryObject> cache) {

		return 0;
	}


}
