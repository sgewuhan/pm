package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class SummaryProjectWorks extends AbstractWorksSummary{

	private List<?> users;

	public SummaryProjectWorks(PrimaryObject po) {
		super(po);
		users = ((Project)po).getParticipatesIdList();
	}

	@Override
	protected Object getMatchCondition(PrimaryObject data) {
		return new BasicDBObject().append(
				WorksPerformence.F_USERID,
				new BasicDBObject().append("$in", users));
	}



}
