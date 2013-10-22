package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.IContext;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

public class WorksPerformence extends AbstractWorksMetadata {

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LOG_16);
	}

	// @Override
	// public String getLabel() {
	// return getDesc()+" "+getLogDate();
	//
	// }

	public String getLogDate() {
		Long dateCode = (Long) getValue(F_DATECODE);
		if (dateCode != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dateCode.longValue() * 24 * 60 * 60 * 1000);
			Date date = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		}
		return null;
	}

	@Override
	public boolean doSave(IContext context) throws Exception {
		super.doSave(context);

		// 合计工作的实际工时
		ObjectId workId = (ObjectId) getValue(F_WORKID);

		DBObject match = new BasicDBObject();
		match.put("$match", new BasicDBObject().append(F_WORKID, workId));

		DBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + F_WORKID).append(
						F_WORKS,
						new BasicDBObject().append("$sum", "$" + F_WORKS)));
		DBCollection col = getCollection();
		AggregationOutput result = col.aggregate(match, group);
		Iterator<DBObject> iter = result.results().iterator();
		if (iter.hasNext()) {
			DBObject data = iter.next();
			Object summary = data.get(F_WORKS);
			if (summary instanceof Double) {
				col = getCollection(IModelConstants.C_WORK);
				col.update(new BasicDBObject().append(Work.F__ID, workId),
						new BasicDBObject().append("$set", new BasicDBObject()
								.append(Work.F_ACTUAL_WORKS, summary)));
			}
		}
		return true;
	}

}
