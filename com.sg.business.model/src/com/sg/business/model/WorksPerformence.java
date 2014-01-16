package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

public class WorksPerformence extends AbstractWorksMetadata {

	private static final String F_CONTENT = "content";

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_LOG_16);
	}

	// @Override
	// public String getLabel() {
	// return getDesc()+" "+getLogDate();
	//
	// }

	@Override
	public String getHTMLLabel() {

		String userId = getStringValue(F_USERID);
		User commiter = UserToolkit.getUserById(userId);
		String desc = getStringValue(F_DESC);// 进展状态
		Date commitdate = getDateValue(F_COMMITDATE);// 提交日期
		Integer works = getIntegerValue(F_WORKS);
		String content = getStringValue(F_CONTENT);
		if(content!=null){
			content = Utils.getLimitLengthString(content, 16);
		}else{
			content = "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-family:微软雅黑;font-size:9pt;'>"); //$NON-NLS-1$
		if(desc.contains("滞后")){
			sb.append("<q style='color:rgb(255,100,100);'>");
		}else{
			sb.append("<q style='color:rgb(0,128,0);'>");
		}
		sb.append(desc);
		sb.append("</q> ");
		sb.append(String.format(Utils.FORMATE_DATE_COMPACT_SASH, commitdate));
		sb.append(" ");
		sb.append(commiter.getUsername());
		sb.append(" ");
		sb.append(" 工时:");
		sb.append(works);
		sb.append("h <q style='color:#b0b0b0'>说明:");
		sb.append(content);
		sb.append("</q></span>");
		return sb.toString();
	}

	public String getLogDate() {
		Long dateCode = (Long) getValue(F_DATECODE);
		if (dateCode != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(dateCode.longValue() * 24 * 60 * 60 * 1000);
			Date date = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
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
		match.put("$match", new BasicDBObject().append(F_WORKID, workId)); //$NON-NLS-1$

		DBObject group = new BasicDBObject();
		group.put(
				"$group", //$NON-NLS-1$
				new BasicDBObject().append("_id", "$" + F_WORKID).append( //$NON-NLS-1$ //$NON-NLS-2$
						F_WORKS,
						new BasicDBObject().append("$sum", "$" + F_WORKS))); //$NON-NLS-1$ //$NON-NLS-2$
		DBCollection col = getCollection();
		AggregationOutput result = col.aggregate(match, group);
		Iterator<DBObject> iter = result.results().iterator();
		if (iter.hasNext()) {
			DBObject data = iter.next();
			Object summary = data.get(F_WORKS);
			if (summary instanceof Double) {
				col = getCollection(IModelConstants.C_WORK);
				col.update(new BasicDBObject().append(Work.F__ID, workId),
						new BasicDBObject().append("$set", new BasicDBObject() //$NON-NLS-1$
								.append(Work.F_ACTUAL_WORKS, summary)));
			}
		}
		return true;
	}

}
