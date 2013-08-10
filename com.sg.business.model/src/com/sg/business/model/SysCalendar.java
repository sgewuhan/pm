package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.utils.DBUtil;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SysCalendar extends PrimaryObject {

	private static final String F_EXPLAIN = "explain";
	private static String F_SEQUENCE = "sequence";

	public SysCalendar() {

	}

	public static List<PrimaryObject> getCondition() {

		PrimaryObject po = null;

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,IModelConstants.C_SYSCALENDAR);
				
		DBCursor cur = col.find();
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		List<DBObject> dbl = cur.toArray();
		if (dbl == null || !(dbl.size() > 0)) {
			DBObject data = new BasicDBObject().append(F__ID, new ObjectId())
					.append(F_SEQUENCE, 0).append(F_DESC, "")
					.append(F_EXPLAIN, "");
			col.insert(data);
			list.add(ModelService.createModelObject(data, BudgetItem.class));
		} else {
			for (DBObject data : dbl) {
				po = ModelService.createModelObject(data, SysCalendar.class);
				list.add(po);
			}
		}

		return list;

	}

	public void createCondition(String condition) {
		DBCursor cur = this.getCollection().find();
		List<DBObject> dblist = cur.toArray();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SYSCALENDAR);
		System.out.println(dblist.size());

	}

	public String getExplain() {

		return (String) this.getValue(F_EXPLAIN);
	}

}
