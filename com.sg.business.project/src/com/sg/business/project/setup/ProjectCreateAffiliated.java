package com.sg.business.project.setup;

//import java.util.Iterator;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkOrderPeriodCost;
//import com.sg.sqldb.utility.SQLResult;
//import com.sg.sqldb.utility.SQLRow;
//import com.sg.sqldb.utility.SQLUtil;

public class ProjectCreateAffiliated implements ISchedualJobRunnable {

	public ProjectCreateAffiliated() {
	}

	@Override
	public boolean run() {
		System.out.println("-----Start-----");
		DBCollection col1 = getCol(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		DBCursor cursor1 = col1.find();
		while (cursor1.hasNext()) {
			DBObject dbObject = (DBObject) cursor1.next();
			BasicDBObject q = new BasicDBObject();
			q.put(WorkOrderPeriodCost.F_WORKORDER, dbObject.get(WorkOrderPeriodCost.F_WORKORDER));
			q.put(WorkOrderPeriodCost.F_MONTH, dbObject.get(WorkOrderPeriodCost.F_MONTH));
			q.put(WorkOrderPeriodCost.F_YEAR, dbObject.get(WorkOrderPeriodCost.F_YEAR));
			q.put(WorkOrderPeriodCost.F_COSTCENTERCODE, dbObject.get(WorkOrderPeriodCost.F_COSTCENTERCODE));
			long count = col1.count(q);
			if(count > 1){
				System.out.print("ObjectId('");
				System.out.print(dbObject.get(WorkOrderPeriodCost.F_WORKORDER));
				System.out.println("'),");
			}
			
		}
		System.out.println("-----End-----");
//		BasicDBObject append = new BasicDBObject().append(Project.F_WORK_ID,
//				null);//$NON-NLS-1$
//		DBCursor cursor = col.find(append);
//		IContext context = new CurrentAccountContext();
//		while (cursor.hasNext()) {
//			DBObject object = cursor.next();
//			Project project = ModelService.createModelObject(object,
//					Project.class);
//			BasicDBObject wbsRootData = new BasicDBObject();
//			wbsRootData.put(Work.F_DESC, project.getDesc());
//			wbsRootData.put(Work.F_LIFECYCLE, project.getLifecycleStatus());
//			wbsRootData.put(Work.F_PROJECT_ID, project.get_id());
//			ObjectId wbsRootId = new ObjectId();
//			wbsRootData.put(Work.F__ID, wbsRootId);
//			wbsRootData.put(Work.F_ROOT_ID, wbsRootId);
//			wbsRootData.put(Work.F_IS_PROJECT_WBSROOT, Boolean.TRUE);
//			wbsRootData.put(Work.F_SETTING_CAN_BREAKDOWN, Boolean.TRUE);
//			Work root = ModelService.createModelObject(wbsRootData, Work.class);
//			try {
//				root.doInsert(context);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			BasicDBObject folderRootData = new BasicDBObject();
//			folderRootData.put(Folder.F_DESC, project.getDesc());
//			folderRootData.put(Folder.F_PROJECT_ID, project.get_id());
//			ObjectId folderRootId = new ObjectId();
//			folderRootData.put(Folder.F__ID, folderRootId);
//			folderRootData.put(Folder.F_ROOT_ID, folderRootId);
//			folderRootData.put(Folder.F_IS_PROJECT_FOLDERROOT, Boolean.TRUE);
//			String containerCollection, containerDB;
//			containerCollection = IModelConstants.C_ORGANIZATION;
//			Container container = Container.adapter(project,
//					Container.TYPE_ADMIN_GRANTED);
//			containerDB = (String) container.getValue(Container.F_SOURCE_DB);
//			folderRootData.put(Folder.F_CONTAINER_DB, containerDB);
//			folderRootData.put(Folder.F_CONTAINER_COLLECTION,
//					containerCollection);
//			Folder folderRoot = ModelService.createModelObject(folderRootData,
//					Folder.class);
//
//			try {
//				folderRoot.doInsert(context);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			DBCollection tgtcol = getCol(IModelConstants.C_BUDGET_ITEM);
//			DBObject srcdata = tgtcol.findOne(new BasicDBObject().append(
//					WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID, null));
//			DBObject tgtData = new BasicDBObject();
//			tgtData.put(ProjectBudget.F_PROJECT_ID, project.get_id());
//			tgtData.put(ProjectBudget.F__ID, new ObjectId());
//			tgtData.put(ProjectBudget.F_DESC, project.getDesc());
//			tgtData.put(ProjectBudget.F_DESC_EN, project.getDesc_e());
//			tgtData.put(ProjectBudget.F_CHILDREN,
//					srcdata.get(BudgetItem.F_CHILDREN));
//			tgtData.put(ProjectBudget.F_BUDGET_VALUE,
//					project.getValue("_budget_code"));//$NON-NLS-1$
//
//			ProjectBudget budget = ModelService.createModelObject(tgtData,
//					ProjectBudget.class);
//			try {
//				budget.doInsert(context);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			BasicBSONList items = (BasicBSONList) project
//					.getValue("_temp_itemcode");//$NON-NLS-1$
//			if (items != null) {
//				for (Object item : items) {
//					ProductItem productItem = ModelService
//							.createModelObject(ProductItem.class);
//					productItem.setValue(ProductItem.F_PROJECT_ID,
//							project.get_id());
//					productItem.setValue(ProductItem.F_DESC, item.toString());
//					try {
//						productItem.doSave(new CurrentAccountContext());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//			DBCollection updatecol = getCol(IModelConstants.C_PROJECT);
//			updatecol
//					.update(new BasicDBObject().append(Project.F__ID,
//							project.get_id()), new BasicDBObject().append(
//							"$set",//$NON-NLS-1$
//							new BasicDBObject().append(Project.F_WORK_ID,
//									root.get_id()).append(Project.F_FOLDER_ID,
//									folderRoot.get_id())));
//		}
//
//		col.update(append, new BasicDBObject().append("$unset", //$NON-NLS-1$
//				new BasicDBObject().append("_temp_itemcode", 1)),//$NON-NLS-1$
//				false, true);
//		col.update(append, new BasicDBObject().append("$unset", //$NON-NLS-1$
//				new BasicDBObject().append("_budget_code", 1)), //$NON-NLS-1$
//				false, true);

//		try {
//			SQLResult result = SQLUtil.SQL_QUERY("budget", "select * from jy");
//			if (!result.isEmpty()) {
//				// 循环构造用户
//				Iterator<SQLRow> iter = result.iterator();
//				while (iter.hasNext()) {
//					SQLRow row = iter.next();
//					String projectnumber = (String) row
//							.getValue("projectnumber");
//					Double budgetvalue = (Double) row.getValue("budgetvalue");
//					BasicDBObject o = new BasicDBObject();
//					o.put(Project.F_PROJECT_NUMBER, projectnumber);
//					DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
//					DBObject findOne = projectCol.findOne(o);
//					if (findOne != null) {
//						ObjectId project_id = (ObjectId) findOne
//								.get(Project.F__ID);
//						if (project_id != null) {
//							DBCollection col = getCol(IModelConstants.C_PROJECT_BUDGET);
//							col.update(
//									new BasicDBObject().append(
//											ProjectBudget.F_PROJECT_ID,
//											project_id),
//									new BasicDBObject().append(
//											"$set",
//											new BasicDBObject()
//													.append(ProjectBudget.F_BUDGET_VALUE,
//															budgetvalue)));
//						}
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return true;

	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
