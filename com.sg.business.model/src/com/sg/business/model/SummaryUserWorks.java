package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;

public class SummaryUserWorks extends AbstractWorksSummary {

	public SummaryUserWorks(PrimaryObject po) {
		super(po);
	}

	@Override
	protected Object getMatchCondition(PrimaryObject data) {
		BasicDBObject condition = new BasicDBObject().append(WorksPerformence.F_USERID,
				((User) data).getUserid());
		
		//是否需要根据项目进行聚合
		if(data.hasKey("$projectid")){ //$NON-NLS-1$
			Object projectid = data.getValue("$projectid"); //$NON-NLS-1$
			condition.put(WorksPerformence.F_PROJECT_ID, projectid);
		}
		
		return condition;
	}
	
	@Override
	protected BasicDBObject getDateCondition(String userid, long dateCode) {
		BasicDBObject condition = super.getDateCondition(userid, dateCode);
		if(data.hasKey("$projectid")){ //$NON-NLS-1$
			Object projectid = data.getValue("$projectid"); //$NON-NLS-1$
			condition.put(WorksPerformence.F_PROJECT_ID, projectid);
		}
		
		return condition;
	}
	
	@Override
	protected Object getGroupCondition(PrimaryObject data) {
		return new BasicDBObject().append("_id", //$NON-NLS-1$
				"$" + WorksPerformence.F_DATECODE).append( //$NON-NLS-1$
				WorksPerformence.F_WORKS,
				new BasicDBObject().append("$sum", "$" //$NON-NLS-1$ //$NON-NLS-2$
						+ WorksPerformence.F_WORKS));
	}

}
