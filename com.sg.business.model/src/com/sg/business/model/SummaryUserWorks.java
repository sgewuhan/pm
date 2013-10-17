package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class SummaryUserWorks extends AbstractWorksSummary {

	public SummaryUserWorks(PrimaryObject po) {
		super(po);
	}

	@Override
	protected Object getMatchCondition(PrimaryObject data) {
		return new BasicDBObject().append(WorksPerformence.F_USERID,
				((User) data).getUserid());
	}

}
