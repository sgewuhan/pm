package com.sg.business.project.setup;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.BudgetItem;
import com.sg.business.model.Container;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.sqldb.utility.SQLResult;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectCreateAffiliated implements ISchedualJobRunnable {

	public ProjectCreateAffiliated() {
	}

	@Override
	public boolean run() {
		// DBCollection col = getCol(IModelConstants.C_PROJECT);
		// DBCursor cursor = col.find(new BasicDBObject().append(
		// Project.F_LIFECYCLE, "finished").append(Project.F_WORK_ID, null));
		// // .append(
		// // Project.F_FUNCTION_ORGANIZATION,
		// // new ObjectId("525b4d18f0209adcc595131f"))
		// IContext context = new CurrentAccountContext();
		// while (cursor.hasNext()) {
		// DBObject object = cursor.next();
		// Project project = ModelService.createModelObject(object,
		// Project.class);
		// BasicDBObject wbsRootData = new BasicDBObject();
		// wbsRootData.put(Work.F_DESC, project.getDesc());
		// wbsRootData.put(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);
		// wbsRootData.put(Work.F_PROJECT_ID, project.get_id());
		// ObjectId wbsRootId = new ObjectId();
		// wbsRootData.put(Work.F__ID, wbsRootId);
		// wbsRootData.put(Work.F_ROOT_ID, wbsRootId);
		// wbsRootData.put(Work.F_IS_PROJECT_WBSROOT, Boolean.TRUE);
		// wbsRootData.put(Work.F_SETTING_CAN_BREAKDOWN, Boolean.TRUE);
		// Work root = ModelService.createModelObject(wbsRootData, Work.class);
		// try {
		// root.doInsert(context);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// BasicDBObject folderRootData = new BasicDBObject();
		// folderRootData.put(Folder.F_DESC, project.getDesc());
		// folderRootData.put(Folder.F_PROJECT_ID, project.get_id());
		// ObjectId folderRootId = new ObjectId();
		// folderRootData.put(Folder.F__ID, folderRootId);
		// folderRootData.put(Folder.F_ROOT_ID, folderRootId);
		// folderRootData.put(Folder.F_IS_PROJECT_FOLDERROOT, Boolean.TRUE);
		// String containerCollection, containerDB;
		// containerCollection = IModelConstants.C_ORGANIZATION;
		// Container container = Container.adapter(project,
		// Container.TYPE_ADMIN_GRANTED);
		// containerDB = (String) container.getValue(Container.F_SOURCE_DB);
		// folderRootData.put(Folder.F_CONTAINER_DB, containerDB);
		// folderRootData.put(Folder.F_CONTAINER_COLLECTION,
		// containerCollection);
		// Folder folderRoot = ModelService.createModelObject(folderRootData,
		// Folder.class);
		//
		// try {
		// folderRoot.doInsert(context);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// DBCollection tgtcol = getCol(IModelConstants.C_BUDGET_ITEM);
		// DBObject srcdata = tgtcol.findOne(new BasicDBObject().append(
		// WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID, null));
		// DBObject tgtData = new BasicDBObject();
		// tgtData.put(ProjectBudget.F_PROJECT_ID, project.get_id());
		// tgtData.put(ProjectBudget.F_DESC, project.getDesc());
		// tgtData.put(ProjectBudget.F_DESC_EN, project.getDesc_e());
		// tgtData.put(ProjectBudget.F_CHILDREN,
		// srcdata.get(BudgetItem.F_CHILDREN));
		//
		// ProjectBudget budget = ModelService.createModelObject(tgtData,
		// ProjectBudget.class);
		// try {
		// budget.doInsert(context);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// DBCollection updatecol = getCol(IModelConstants.C_PROJECT);
		// updatecol
		// .update(new BasicDBObject().append(Project.F__ID,
		// project.get_id()), new BasicDBObject().append(
		// "$set",
		// new BasicDBObject().append(Project.F_WORK_ID,
		// root.get_id()).append(Project.F_FOLDER_ID,
		// folderRoot.get_id())));
		// }

		try {
			SQLResult result = SQLUtil
					.SQL_QUERY("budget", "select * from kh");
			if (!result.isEmpty()) {
				// 循环构造用户
				Iterator<SQLRow> iter = result.iterator();
				while (iter.hasNext()) {
					SQLRow row = iter.next();
					String projectnumber = (String) row
							.getValue("projectnumber");
					String desc = (String) row.getValue("desc");
					Double budgetvalue = (Double) row.getValue("budgetvalue");
					BasicDBObject o = new BasicDBObject();
					o.put(Project.F_PROJECT_NUMBER, projectnumber);
					o.put(Project.F_DESC, desc);
					DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
					DBObject findOne = projectCol.findOne(o);
					if (findOne != null) {
						ObjectId project_id = (ObjectId) findOne
								.get(Project.F__ID);
						if (project_id != null) {
							DBCollection col = getCol(IModelConstants.C_PROJECT_BUDGET);
							col.update(
									new BasicDBObject().append(
											ProjectBudget.F_PROJECT_ID,
											project_id),
									new BasicDBObject().append(
											"$set",
											new BasicDBObject()
													.append(ProjectBudget.F_BUDGET_VALUE,
															budgetvalue)));
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

//		List<ObjectId> delectProjectIdList = new ArrayList<ObjectId>();
//		DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
//		DBCollection projectCol1 = getCol(IModelConstants.C_PROJECT);
//		DBCursor projectCursor = projectCol.find();
//		while (projectCursor.hasNext()) {
//			DBObject object = projectCursor.next();
//			Project project = ModelService.createModelObject(object,
//					Project.class);
//			DBCursor projectCursor1 = projectCol1
//					.find(new BasicDBObject()
//							.append(Project.F__ID,
//									new BasicDBObject().append("$ne",
//											project.get_id())).append(
//									Project.F_DESC, project.getDesc()));
//		}

		return true;

	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
