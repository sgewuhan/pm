package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
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

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SYSCALENDAR);
		DBCursor cur = col.find();
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		List<DBObject> dbl = cur.toArray();
		for (DBObject data : dbl) {
			list.add(ModelService.createModelObject(data, SysCalendar.class));
		}
		return list;

	}

	public static void createCondition(String condition) {

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SYSCALENDAR);
		DBCursor cur = col.find();
		DBObject data = new BasicDBObject().append(F__ID, new ObjectId())
				.append(F_SEQUENCE, cur.size()).append(F_DESC, condition)
				.append(F_EXPLAIN, "");
		col.insert(data);
		ModelService.createModelObject(data, BudgetItem.class);

	}


	public void createFollowCondition(String condition) {
		DBCollection col = this.getCollection();
		
		DBObject data = new BasicDBObject().append(F__ID, new ObjectId())
				.append(F_SEQUENCE, (Integer)this.getValue(F_SEQUENCE)+1).append(F_DESC, condition)
				.append(F_EXPLAIN, "");
		col.insert(data);
		changeSequence(data);
	}

	public String getSequence() {
		return this.getValue(F_SEQUENCE).toString();
	}
	
	public String getExplain() {

		return (String) this.getValue(F_EXPLAIN);
	}

    public void changeSequence(DBObject data){
    	
    	DBCollection col = DBActivator.getCollection(IModelConstants.DB,IModelConstants.C_SYSCALENDAR);
		List<DBObject> dataList = col.find().toArray();
		for(DBObject db:dataList){
			if(data.get(PrimaryObject.F__ID).equals(db.get(PrimaryObject.F__ID))){
				continue;
			}
			if((Integer)(data.get("sequence"))-(Integer)(db.get("sequence"))==0){
		      	PrimaryObject po=ModelService.createModelObject(db, BudgetItem.class);
		        po.setValue(F_SEQUENCE,(Integer)(po.getValue(F_SEQUENCE))+1);
		      	System.out.println(	po.getValue(F_SEQUENCE));
		      	changeSequence(db);
			}
		}
    }
	
}
