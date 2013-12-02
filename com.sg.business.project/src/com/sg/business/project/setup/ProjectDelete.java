package com.sg.business.project.setup;

//import java.util.Date;
//import java.util.List;
//
//import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
//import com.mobnut.db.DBActivator;
//import com.mobnut.db.model.ModelService;
//import com.mobnut.db.model.PrimaryObject;
//import com.mongodb.BasicDBObject;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//import com.sg.business.model.Deliverable;
//import com.sg.business.model.Document;
//import com.sg.business.model.IModelConstants;
//import com.sg.business.model.Work;
//import com.sg.business.model.WorksPerformence;
//import com.sg.widgets.part.CurrentAccountContext;

public class ProjectDelete implements ISchedualJobRunnable {
	// private static ObjectId[] DELETELIST = new ObjectId[] {
	// new ObjectId("5281cb76e0cc49249f3c6715"),
	// new ObjectId("528476a0e0cc7c1c547d58d5") };

	public ProjectDelete() {
	}

	@Override
	public boolean run() {
		// DBCollection col = getCol();
		// for (ObjectId _id : DELETELIST) {

		// Organization org = ModelService.createModelObject(
		// Organization.class, _id);
		// List<PrimaryObject> projectList = org.getRelationById(
		// Organization.F__ID, Project.F_FUNCTION_ORGANIZATION,
		// Project.class);
		// for (PrimaryObject po : projectList) {
		// Project project = (Project) po;
		// Project project = ModelService
		// .createModelObject(Project.class, _id);
		//
		// try {
		// project.doRemove(new CurrentAccountContext());
		// } catch (Exception e) {
		// }
		// col.remove(new BasicDBObject().append(Project.F__ID,
		// project.get_id()));
		// }
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
	// IModelConstants.C_PROJECT);
	// }

	// private DBCollection getCol(String collectionName) {
	// return DBActivator.getCollection(IModelConstants.DB, collectionName);
	// }

}
