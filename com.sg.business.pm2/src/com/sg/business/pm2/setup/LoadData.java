package com.sg.business.pm2.setup;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;

public class LoadData implements ISchedualJobRunnable {
	String DB1 = "pm2-test";// 新数据
	String DB2 = "pm2";// 历史数据

	public LoadData() {
	}

	@Override
	public boolean run() throws Exception {
		initOrganization();
		initUser();

		return false;
	}

	@SuppressWarnings("unchecked")
	private void initUser() {
		DBCollection db1UserCol = getCol(DB1, IModelConstants.C_USER);
		DBCollection db2UserCol = getCol(DB2, IModelConstants.C_USER);

		List<Object> db1user_Ids = db1UserCol.distinct(User.F_USER_ID);
		List<Object> db2user_Ids = db2UserCol.distinct(User.F_USER_ID);

		List<String> removeOrg_Ids = (List<String>) checkRemoveId(db1user_Ids,
				db2user_Ids);
		db2UserCol.update(new BasicDBObject().append(User.F_USER_ID,
				new BasicDBObject().append("$in", removeOrg_Ids)),
				new BasicDBObject().append("$set", new BasicDBObject().append(
						User.F_ACTIVATED, Boolean.FALSE)), false, true);

		db2UserCol.update(
				new BasicDBObject().append(User.F_ACTIVATED, Boolean.FALSE),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject()
								.append(User.F_ORGANIZATION_ID, null)
								.append(User.F_ORGANIZATION_NAME, null)
								.append("_temp", null)));

		DBCursor db1Cursor = db1UserCol.find();
		while (db1Cursor.hasNext()) {
			DBObject user1Data = db1Cursor.next();
			Object user1_id = user1Data.get(User.F_USER_ID);
			DBObject user2Data = db2UserCol.findOne(new BasicDBObject().append(
					User.F_USER_ID, user1_id));
			if (user2Data == null) {
				db2UserCol.insert(user1Data, WriteConcern.NORMAL);
			} else {
				BasicDBObject updateData = new BasicDBObject();
				Object o1 = user1Data.get(User.F_ACTIVATED);
				Object o2 = user2Data.get(User.F_ACTIVATED);
				if (!o1.equals(o2)) {
					updateData.put(User.F_ACTIVATED, o1);
				}
				o1 = user1Data.get(User.F_ORGANIZATION_ID);
				o2 = user2Data.get(User.F_ORGANIZATION_ID);
				if (!o1.equals(o2)) {
					updateData.put(User.F_ORGANIZATION_ID, o1);
					updateData.put(User.F_ORGANIZATION_NAME,
							user1Data.get(User.F_ORGANIZATION_NAME));
					updateData.put("_temp", user1Data.get("_orgnumber"));
				}
				o1 = user1Data.get(User.F_SCENARIO);
				updateData.put(User.F_SCENARIO, o1);
				if (!updateData.isEmpty()) {
					db2UserCol.update(new BasicDBObject().append(
							User.F_USER_ID, user1_id), new BasicDBObject()
							.append("$set", updateData));
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void initOrganization() {
		DBCollection db1OrganizationCol = getCol(DB1,
				IModelConstants.C_ORGANIZATION);
		DBCollection db2OrganizationCol = getCol(DB2,
				IModelConstants.C_ORGANIZATION);
		List<Object> db1org_Ids = db1OrganizationCol
				.distinct(Organization.F__ID);
		List<Object> db2org_Ids = db2OrganizationCol
				.distinct(Organization.F__ID);

		List<ObjectId> removeOrg_Ids = (List<ObjectId>) checkRemoveId(
				db1org_Ids, db2org_Ids);
		db2OrganizationCol.remove(new BasicDBObject().append(
				Organization.F__ID,
				new BasicDBObject().append("$in",
						removeOrg_Ids.toArray(new ObjectId[0]))));

		DBCursor db1Cursor = db1OrganizationCol.find();
		while (db1Cursor.hasNext()) {
			DBObject org1Data = db1Cursor.next();
			Object org1_id = org1Data.get(Organization.F__ID);
			DBObject org2Data = db2OrganizationCol.findOne(new BasicDBObject()
					.append(Organization.F__ID, org1_id));
			if (org2Data == null) {
				db2OrganizationCol.insert(org1Data, WriteConcern.NORMAL);
			} else {
				BasicDBObject updateData = new BasicDBObject();
				Object o1 = org1Data.get(Organization.F_FULLDESC);
				Object o2 = org2Data.get(Organization.F_FULLDESC);
				if (!o1.equals(o2)) {
					updateData.put(Organization.F_FULLDESC, o1);
				}
				o1 = org1Data.get(Organization.F_ORGANIZATION_NUMBER);
				o2 = org2Data.get(Organization.F_ORGANIZATION_NUMBER);
				if (!o1.equals(o2)) {
					updateData.put(Organization.F_ORGANIZATION_NUMBER, o1);
				}
				o1 = org1Data.get(Organization.F_PARENT_ID);
				o2 = org2Data.get(Organization.F_PARENT_ID);
				if (o1 != null) {
					if (o1.equals(o2)) {
						updateData.put(Organization.F_PARENT_ID, o1);
					}
				}
				if (!updateData.isEmpty()) {
					db2OrganizationCol.update(new BasicDBObject().append(
							Organization.F__ID, org1_id), new BasicDBObject()
							.append("$set", updateData));
				}
			}

		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<?> checkRemoveId(List<Object> db1org_Ids,
			List<Object> db2org_Ids) {
		List<Object> tempList = new ArrayList();
		tempList.addAll(db2org_Ids);
		tempList.removeAll(db1org_Ids);
		return tempList;
	}

	private DBCollection getCol(String dbName, String collectionName) {
		return DBActivator.getCollection(dbName, collectionName);
	}

}
