package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SummaryUserWorks implements IWorksSummary {

	private User user;
	private AggregationOutput result;
	private DBCollection col;

	public SummaryUserWorks(PrimaryObject po) {
		this.user = (User) po;
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORKS_PERFORMENCE);
	}

	@Override
	public double getWorksSummary(Date start, Date end) {
		long startDateValue = start.getTime() / (24 * 60 * 60 * 1000);
		long endDateValue = end.getTime() / (24 * 60 * 60 * 1000);

		double ret = 0d;
		
		if (result == null) {
			aggregation();
		}
		Iterator<DBObject> iter = result.results().iterator();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			Object id = data.get("_id");
			if ((id instanceof Number)
					&& (((Number) id).longValue() >= startDateValue && ((Number) id)
							.longValue() <= endDateValue)) {
				Object val = data.get(WorksPerformence.F_WORKS);
				if (val instanceof Double) {
					ret += ((Double) val).doubleValue();
				}
			}

		}

		return ret;
	}

	@Override
	public double getWorksSummaryOfDay(Date date) {
		if (result == null) {
			aggregation();
		}
		
		
		long dateValue = date.getTime() / (24 * 60 * 60 * 1000);
		
		Iterator<DBObject> iter = result.results().iterator();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			Object id = data.get("_id");
			if ((id instanceof Number)
					&& (((Number) id).longValue() == dateValue)) {
				Object val = data.get(WorksPerformence.F_WORKS);
				if (val instanceof Double) {
					return ((Double) val).doubleValue();
				}
			}
		}
		return 0d;
	}

	private void aggregation() {
		// 获取该用户的工作，进行中，已完成的

		DBObject match = new BasicDBObject();
		match.put(
				"$match",
				new BasicDBObject().append(WorksPerformence.F_USERID,
						user.getUserid()));

		DBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id",
						"$" + WorksPerformence.F_DATECODE).append(
						WorksPerformence.F_WORKS,
						new BasicDBObject().append("$sum", "$"
								+ WorksPerformence.F_WORKS)));

		result = col.aggregate(match, group);
	}
	
	@Override
	public List<PrimaryObject[]> getWorkOfWorksSummaryOfDateCode(String userid,
			Date date) {
		long dateCode = date.getTime() / (24 * 60 * 60 * 1000);
		
		
		ArrayList<PrimaryObject[]> ret = new ArrayList<PrimaryObject[]>();
		DBCursor cur = col.find(
				new BasicDBObject().append(WorksPerformence.F_USERID, userid)
						.append(WorksPerformence.F_DATECODE, dateCode),
				new BasicDBObject().append(WorksPerformence.F_WORKID, 1));
		while (cur.hasNext()) {
			DBObject next = cur.next();
			ObjectId workid = (ObjectId) next.get(WorksPerformence.F_WORKID);
			ObjectId _id = (ObjectId) next.get(WorksPerformence.F__ID);
			Work work = ModelService.createModelObject(Work.class, workid);
			WorksPerformence wp = ModelService.createModelObject(
					WorksPerformence.class, _id);
			ret.add(new PrimaryObject[] { work, wp });
		}

		return ret;
	}

}
