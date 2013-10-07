package com.sg.business.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.toolkit.UserToolkit;

public class WorkRecord extends PrimaryObject {

	// public static final String F_USERID = "userid";
	// public static final String F_RDATE = "rdate";
	// public static final String F_PERCENT = "percent";
	// public static final String F_WORK_ID = "work_id";
	//
	// @Override
	// public void doInsert(IContext context) throws Exception {
	// ObjectId workid=(ObjectId) getValue(F_WORK_ID);
	// Work work=ModelService.createModelObject(Work.class, workid);
	// BasicDBList dataList = (BasicDBList) work.getValue(Work.F_RECORD);
	// if (dataList == null) {
	// dataList = new BasicDBList();
	// }
	//
	// BasicDBObject record=new BasicDBObject();
	// record.put(F_USERID,getValue(F_USERID));
	// record.put(F_RDATE, getValue(F_RDATE));
	// record.put(F_PERCENT, getValue(F_PERCENT));
	// record.put(F_DESC, getValue(F_DESC));
	// dataList.add(record);
	//
	// DBCollection col = DBActivator.getCollection(IModelConstants.DB,
	// IModelConstants.C_WORK);
	// col.update(new BasicDBObject().append("_id", getValue(F_WORK_ID)),new
	// BasicDBObject().append("$set",
	// new BasicDBObject().append(Work.F_RECORD,dataList)));
	//
	// }

	public static final String F_WORK_ID = "work_id";

	public static final String F_RECORDERID = "userid";

	public static final String F_RECORDDATE = "recorddate";

	public static final String F_CONTENT = "content";

	public static final String F_WORKS_FINISHED_PERCENT = "worksfinishedpercent";

	@Override
	public void doInsert(IContext context) throws Exception {
		ObjectId workId = (ObjectId) getValue(F_WORK_ID);

		DBCollection col = getCollection(IModelConstants.C_WORK);

		col.update(
				new BasicDBObject().append(Work.F__ID, workId),
				new BasicDBObject().append("$push",
						new BasicDBObject().append(Work.F_RECORD, get_data())));
	}

	@Override
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt'><b>");

		sb.append("<span style='float:right;padding-right:4px'>");
		Object value = getValue(F_RECORDERID);
		if (value instanceof String) {
			User user = UserToolkit.getUserById((String) value);
			sb.append(user);
			sb.append("  ");
		}

		sb.append("</span>");

		sb.append(getDesc());

		sb.append("  ");

		value = getValue(F_WORKS_FINISHED_PERCENT);
		if (value instanceof Double) {
			DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2_PERC);
			String finished = df.format(value);
			sb.append(finished);
		}

		sb.append("</b></span>");

		sb.append("<br/><small>");

		sb.append("<span style='float:right;padding-right:4px'>");
		value = getValue(F_RECORDDATE);
		if (value instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(
					Utils.SDF_DATETIME_COMPACT_SASH);
			String recordDate = sdf.format(value);
			sb.append(recordDate);
		}
		sb.append("</span>");

		Object content = getValue(F_CONTENT);
		if (content != null) {
			sb.append(content);
		}

		sb.append("</small>");

		return sb.toString();
	}
}
