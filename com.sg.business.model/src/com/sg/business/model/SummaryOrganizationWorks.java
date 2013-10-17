package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class SummaryOrganizationWorks extends AbstractWorksSummary{

	private List<String> users;

	public SummaryOrganizationWorks(PrimaryObject po) {
		super(po);
		users = ((Organization)po).getMemberIds(true);
	}

	@Override
	protected Object getMatchCondition(PrimaryObject data) {
		return new BasicDBObject().append(
				WorksPerformence.F_USERID,
				new BasicDBObject().append("$in", users));
	}

	@Override
	protected Object getGroupCondition(PrimaryObject data) {
		return new BasicDBObject().append("_id",
				"$" + WorksPerformence.F_DATECODE).append(
				WorksPerformence.F_WORKS,
				new BasicDBObject().append("$sum", "$"
						+ WorksPerformence.F_WORKS));
	}



}
