package com.sg.business.project.setup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.sqldb.utility.SQLResult;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;

public class ProjectDelete implements ISchedualJobRunnable {
	// private static ObjectId[] DELETELIST = new ObjectId[] {
	// new ObjectId("5281cb76e0cc49249f3c6715"),
	// new ObjectId("528476a0e0cc7c1c547d58d5") };

	public ProjectDelete() {
	}

	@Override
	public boolean run() {

//		DBCollection deliverableCol = getCol(IModelConstants.C_DELIEVERABLE);
//		DBCollection documentCol = getCol(IModelConstants.C_DOCUMENT);
//		DBCursor deliverableCursor = deliverableCol.find();
//		while (deliverableCursor.hasNext()) {
//			DBObject deliverableData = deliverableCursor.next();
//			Deliverable deliverable = ModelService.createModelObject(deliverableData, Deliverable.class);
//			ObjectId document_id = deliverable.getDocumentId();
//			long count = documentCol.count(new BasicDBObject().append(Document.F__ID, document_id));
//			if(count==0){
//				try {
//					deliverable.doRemove(new CurrentAccountContext());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}

		SQLResult result;
		DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
		String projectnumber = "";
		try {
			result = SQLUtil.SQL_QUERY("budget", //$NON-NLS-1$
					"select * from jy2 ");
			if (!result.isEmpty()) {
				Iterator<SQLRow> iter = result.iterator();
				while (iter.hasNext()) {
					SQLRow row = iter.next();
					projectnumber = "" + row.getValue("projectnumber");
					BasicDBObject updateData = new BasicDBObject();
					DBObject dbo = projectCol.findOne(new BasicDBObject()
							.append(Project.F_PROJECT_NUMBER, projectnumber));
					ObjectId project_id = (ObjectId) dbo.get(Project.F__ID);
					String productItem = "" + row.getValue("productitem");
					if (productItem != "") {
						String[] productItems = productItem.split(",");
						List<String> productItemList = new ArrayList<String>();
						for (String productItemString : productItems) {
							productItemString = productItemString.trim();
							productItemList.add(productItemString);
						}
						updateData.put("_temp_itemcode", productItemList);
					}
					projectCol.update(new BasicDBObject().append(Project.F__ID,
							project_id), new BasicDBObject().append("$set",
							updateData));
				}
			}
		} catch (Exception e) {
			System.out.println(projectnumber);
			e.printStackTrace();
		}

		// DBCollection col1 = getCol(IModelConstants.C_WORK);
		// DBCollection col2 = getCol(IModelConstants.C_WORK);
		//
		// BasicDBObject ref = new BasicDBObject();
		// ref.put(Work.F_IS_PROJECT_WBSROOT,
		// new BasicDBObject().append("$ne", Boolean.TRUE));
		// ref.put(Work.F_WORK_TYPE,
		// new BasicDBObject().append("$ne", Work.WORK_TYPE_STANDLONE));
		// DBCursor cursor = col1.find(ref);
		// while (cursor.hasNext()) {
		// DBObject workData = cursor.next();
		// ObjectId parent_id = (ObjectId) workData.get(Work.F_PARENT_ID);
		// BasicDBObject query = new BasicDBObject();
		// query.put(Work.F__ID, parent_id);
		// long count = col2.count(query);
		// if (count == 0) {
		// System.out.println("ObjectId('" + workData.get(Work.F__ID)
		// + "')");
		// }
		// }

		// DBCollection col = getCol();
		// ObjectId _id = new ObjectId("525b4d18f0209adcc595131f");
		//
		// Organization org = ModelService.createModelObject(Organization.class,
		// _id);
		// List<PrimaryObject> projectList = org.getRelationById(
		// Organization.F__ID, Project.F_FUNCTION_ORGANIZATION,
		// Project.class);
		// DBCollection projectCol = getCol();
		// BasicDBObject ref = new BasicDBObject();
		// ref.put(ProjectMonthData.F_YEAR, 2013);
		// ref.put(ProjectMonthData.F_MONTH, 11);
		//
		// DBCursor cursor = projectCol.find(ref);
		// while (cursor.hasNext()) {
		// DBObject next = cursor.next();
		// ObjectId object = (ObjectId) next.get(ProjectMonthData.F_PROJECTID);
		// ref.put(ProjectMonthData.F_PROJECTID, object);
		// long count = col.count(ref);
		// if (count > 1) {
		// col.remove(next);
		// }
		// }

		// List<PrimaryObject> projectList = null;
		// for (PrimaryObject po : projectList) {
		// Project project = (Project) po;
		// // Project project = ModelService
		// // .createModelObject(Project.class, _id);
		// String lc = project.getLifecycleStatus();
		// Work wbsRoot = project.getWBSRoot();
		// String worklc = wbsRoot.getLifecycleStatus();
		//
		// if(lc != null && !lc.equals(worklc)){
		// DBCollection updatecol = getCol(IModelConstants.C_WORK);
		// updatecol
		// .update(new BasicDBObject().append(Work.F__ID,
		// wbsRoot.get_id()), new BasicDBObject().append(
		// "$set",
		// new BasicDBObject().append(Work.F_LIFECYCLE,lc)));
		// }

		// try {
		// project.doRemove(new CurrentAccountContext());
		// } catch (Exception e) {
		// }
		// col.remove(new BasicDBObject().append(Project.F__ID,
		// project.get_id()));
		// }

		// DBCollection col = getCol(IModelConstants.C_WORK);
		// DBCollection workCol = getCol(IModelConstants.C_WORK);
		// DBCollection doccol = getCol(IModelConstants.C_DOCUMENT);
		// DBCollection delcol = getCol(IModelConstants.C_DELIEVERABLE);
		// DBCursor cursor = col.find(new
		// BasicDBObject().append(Work.F_WORK_TYPE,
		// Work.WORK_TYPE_STANDLONE));
		// while (cursor.hasNext()) {
		// DBObject next = cursor.next();
		// Work work = ModelService.createModelObject(next, Work.class);
		// // 删除交付物
		// delcol.remove(new BasicDBObject().append(Deliverable.F_WORK_ID,
		// work.get_id()));
		//
		// // 删除文档
		// doccol.remove(new BasicDBObject().append(Document.F_WORK_ID,
		// work.get_id()));
		//
		// // 删除work
		// workCol.remove(new BasicDBObject().append(Work.F__ID,
		// work.get_id()));
		// }

		// ObjectId rootId = new ObjectId("4f7028202c326f67f775ef03");
		// Work rootWork = ModelService.createModelObject(Work.class, rootId);
		// List<PrimaryObject> childrenWorkList = rootWork.getChildrenWork();
		// for (PrimaryObject po : childrenWorkList) {
		// Work childrenWork = (Work) po;
		// List<PrimaryObject> childrenWorkList2 = childrenWork
		// .getChildrenWork();
		// for (PrimaryObject po2 : childrenWorkList2) {
		// Work childrenWork2 = (Work) po2;
		// doWorkSperformence(childrenWork2);
		// }
		// }

		// DBCollection col = getCol(IModelConstants.C_WORKS_PERFORMENCE);
		// DBCollection updatecol = getCol(IModelConstants.C_WORKS_PERFORMENCE);
		// DBCursor cursor = col.find();
		// while (cursor.hasNext()) {
		// DBObject next = cursor.next();
		// BasicDBObject q = new BasicDBObject();
		// q.put(WorksPerformence.F__ID, next.get(WorksPerformence.F__ID));
		// Date object = (Date) next.get(WorksPerformence.F_COMMITDATE);
		// BasicDBObject o = new BasicDBObject();
		// o.put("$set",
		// new BasicDBObject().append(WorksPerformence.F_DATECODE,
		// new Long(object.getTime() / (24 * 60 * 60 * 1000)))
		// .append(WorksPerformence.F__CDATE, object));
		//
		// updatecol.update(q, o);
		// }

		// DBCollection col = getCol();
		// ObjectId org_id = new ObjectId("525cede9ede04bec14551263");
		// Organization org = ModelService.createModelObject(Organization.class,
		// org_id);
		// List<PrimaryObject> list = org.getRelationById(Organization.F__ID,
		// Project.F_FUNCTION_ORGANIZATION, Project.class);
		// for (PrimaryObject po : list) {
		// Project project = (Project) po;
		// User charger = project.getCharger();
		// if (charger != null) {
		// ObjectId organization_id = charger.getOrganization_id();// new
		// // ObjectId("525c9807ede0fb240744a593");
		// BasicBSONList launchorg_id = new BasicBSONList();
		// launchorg_id.add(organization_id);
		// col.update(new BasicDBObject().append(Project.F__ID,
		// project.get_id()), new BasicDBObject().append("$set",
		// new BasicDBObject().append(
		// Project.F_LAUNCH_ORGANIZATION, launchorg_id)));
		// }
		// }

		return true;
	}

	// private void doWorkSperformence(Work childrenWork) {
	// if (childrenWork.hasChildrenWork()) {
	// List<PrimaryObject> childrenWorkList2 = childrenWork
	// .getChildrenWork();
	// for (PrimaryObject po2 : childrenWorkList2) {
	// Work childrenWork2 = (Work) po2;
	// doWorkSperformence(childrenWork2);
	// }
	// } else {
	// WorksPerformence data = ModelService
	// .createModelObject(WorksPerformence.class);
	// data.setValue(WorksPerformence.F_WORKID, childrenWork.get_id());
	// data.setValue(WorksPerformence.F_USERID,
	// childrenWork.getChargerId());
	// data.setValue(WorksPerformence.F_COMMITDATE,
	// childrenWork.getActualFinish());
	// data.setValue(WorksPerformence.F_DATECODE, 1);
	// data.setValue(WorksPerformence.F_PLANWORKS, childrenWork
	// .getProject().getDesc());
	// data.setValue(WorksPerformence.F_PROJECT_ID,
	// childrenWork.getProjectId());
	// data.setValue(WorksPerformence.F_WORKDESC, childrenWork.getDesc());
	// data.setValue(WorksPerformence.F__EDITOR,
	// "editor.create.workrecord");
	// data.setValue(WorksPerformence.F_DESC, "正常进行");
	// data.setValue(WorksPerformence.F_WORKS, 1.00);
	// data.setValue("worksfinishedpercent", 1.00);
	//
	// try {
	// data.doSave(new CurrentAccountContext());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// private DBCollection getCol() {
	// return DBActivator.getCollection(IModelConstants.DB,
	// IModelConstants.C_PROJECT_MONTH_DATA);
	// }

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
