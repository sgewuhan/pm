package com.sg.business.model;

import java.util.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SummaryUserWorks implements IWorksSummary {

	private User user;

	public SummaryUserWorks(PrimaryObject po) {
		this.user = (User) po;
	}

	@Override
	public double getWorksSummary(Date start, Date end) {
		return 0;
	}

	@Override
	public double getWorksSummaryOfDay(Date date, HashMap<ObjectId,PrimaryObject> cache) {
		// 获取该用户的工作，进行中，已完成的
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);

		BasicDBObject condition = new BasicDBObject();
		condition.put(Work.F_PARTICIPATE, user.getUserid());
		condition.put(
				Work.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						Work.STATUS_FINIHED_VALUE, Work.STATUS_WIP_VALUE }));

		BasicDBObject fields = new BasicDBObject();
		fields.put(Work.F_LIFECYCLE, 1);
		fields.put(Work.F_PARTICIPATE, 1);
		fields.put(Work.F_PLAN_START, 1);
		fields.put(Work.F_PLAN_FINISH, 1);
		fields.put(Work.F_PLAN_WORKS, 1);
		fields.put(Work.F_PLAN_DURATION, 1);
		fields.put(Work.F_ACTUAL_START, 1);
		fields.put(Work.F_ACTUAL_FINISH, 1);
		fields.put(Work.F_ACTUAL_WORKS, 1);
		fields.put(Work.F_ACTUAL_DURATION, 1);
		fields.put(Work.F_PERFORMENCE, 1);

		double result = 0d;
		DBCursor cur = col.find(condition, fields);
		
		while (cur.hasNext()) {
			DBObject data = cur.next();
			ObjectId id = (ObjectId) data.get(Work.F__ID);
			Work work = (Work) cache.get(id);
			if(work ==null){
				work = ModelService.createModelObject(data,Work.class);
				cache.put(id, work);
			}
			
			if(work!=null){
				result += work.getParticipatesActualWorks(user.getUserid(),date);
			}
		}

		return result;
	}

}
