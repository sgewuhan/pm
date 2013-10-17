package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class SummaryProjectWorks extends AbstractWorksSummary {

	public SummaryProjectWorks(PrimaryObject po) {
		super(po);
	}

	@Override
	protected Object getMatchCondition(PrimaryObject data) {
		return new BasicDBObject().append(WorksPerformence.F_PROJECTID,
				data.get_id());
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
