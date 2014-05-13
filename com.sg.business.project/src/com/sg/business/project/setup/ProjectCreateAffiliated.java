package com.sg.business.project.setup;


import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;

public class ProjectCreateAffiliated implements ISchedualJobRunnable {

	public ProjectCreateAffiliated() {
	}

	@Override
	public boolean run() {
		// System.out.println("-----Start-----");
		// DBCollection col1 =
		// getCol(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		// DBCursor cursor1 = col1.find();
		// while (cursor1.hasNext()) {
		// DBObject dbObject = (DBObject) cursor1.next();
		// BasicDBObject q = new BasicDBObject();
		// q.put(WorkOrderPeriodCost.F_WORKORDER,
		// dbObject.get(WorkOrderPeriodCost.F_WORKORDER));
		// q.put(WorkOrderPeriodCost.F_MONTH,
		// dbObject.get(WorkOrderPeriodCost.F_MONTH));
		// q.put(WorkOrderPeriodCost.F_YEAR,
		// dbObject.get(WorkOrderPeriodCost.F_YEAR));
		// q.put(WorkOrderPeriodCost.F_COSTCENTERCODE,
		// dbObject.get(WorkOrderPeriodCost.F_COSTCENTERCODE));
		// long count = col1.count(q);
		// if(count > 1){
		// System.out.print("ObjectId('");
		// System.out.print(dbObject.get(WorkOrderPeriodCost.F_WORKORDER));
		// System.out.println("'),");
		// }
		//
		// }
		// System.out.println("-----End-----");
		// List<String> projectNumberList = new ArrayList<String>();
		// // projectNumberList.add("TBKF1310A");//B
		// // projectNumberList.add("TBKF13249");//A
		// // projectNumberList.add("TBKF131BE");//B
		// // projectNumberList.add("TBKF132A1");//B
		// // projectNumberList.add("TBKF132A3");//A
		// // projectNumberList.add("TBKF132BF");//B
		// // projectNumberList.add("TBKF131D5");//A
		// projectNumberList.add("TBKF132AC");//A
		// //// projectNumberList.add("TBKF14001");//B
		// //// projectNumberList.add("TBKF14002");//B
		// //// projectNumberList.add("TBKF14007");//B
		// //// projectNumberList.add("TBKF1400A");//B
		// //// projectNumberList.add("TBKF14011");//B
		// //// projectNumberList.add("TBKF14046");//A
		// //// projectNumberList.add("TBKF14047");//A
		// //// projectNumberList.add("TBKF1404B");//B
		// //// projectNumberList.add("TBKF14054");//B
		// //// projectNumberList.add("TBKF14055");//B
		// //// projectNumberList.add("TBKF14059");//A
		//
		// IContext context;
		// try {
		// DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
		// BasicDBObject append = new BasicDBObject()
		// .append(Project.F_PROJECT_NUMBER, new BasicDBObject()
		// .append("$in", projectNumberList));
		// DBCursor projectCursor = projectCol.find(append);
		// while (projectCursor.hasNext()) {
		// DBObject object = projectCursor.next();
		// Project project = ModelService.createModelObject(object,
		// Project.class);
		// final User charger = project.getCharger();
		// context = new IContext() {
		//
		// @Override
		// public AccountInfo getAccountInfo() {
		// return new AccountInfo(charger.getUsername(),
		// charger.getUserid());
		// }
		//
		// @Override
		// public String getPartId() {
		// return null;
		// }
		//
		// };
		//
		// ProjectTemplate projectTemplate = project.getTemplate();
		// Map<ObjectId, DBObject> roleMap = project
		// .doMakeRolesWithTemplate(projectTemplate.get_id(),
		// context);
		//
		// Collection<DBObject> values = roleMap.values();
		// Iterator<DBObject> iter = values.iterator();
		//
		// Set<DBObject> toBeInsert = new HashSet<DBObject>();
		// while (iter.hasNext()) {
		// DBObject prole = iter.next();
		// Object roleId = prole
		// .get(ProjectRole.F_ORGANIZATION_ROLE_ID);
		// if (roleId != null) {
		// // 将组织角色中的成员加入到项目的参与者
		// Role role = ModelService.createModelObject(Role.class,
		// (ObjectId) roleId);
		// IRoleParameter roleParameter = project
		// .getAdapter(IRoleParameter.class);
		//
		// List<PrimaryObject> ass = role
		// .getAssignment(roleParameter);
		// project.doAddParticipateFromAssignment(ass);
		// }
		//
		//					prole.removeField("used"); //$NON-NLS-1$
		// toBeInsert.add(prole);
		// }
		//
		// DBObject[] insertData = toBeInsert.toArray(new DBObject[0]);
		// // 插入到数据库
		// DBCollection col_role = getCol(IModelConstants.C_PROJECT_ROLE);
		// col_role.insert(insertData, WriteConcern.NORMAL);
		//
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// DBCollection col = getCol(IModelConstants.C_PROJECT);
		// BasicDBObject append = new BasicDBObject().append(Project.F_WORK_ID,
		//				null);//$NON-NLS-1$
		// DBCursor cursor = col.find(append);
		// IContext context = new CurrentAccountContext();
		// while (cursor.hasNext()) {
		// DBObject object = cursor.next();
		// Project project = ModelService.createModelObject(object,
		// Project.class);
		// BasicDBObject wbsRootData = new BasicDBObject();
		// wbsRootData.put(Work.F_DESC, project.getDesc());
		// wbsRootData.put(Work.F_LIFECYCLE, project.getLifecycleStatus());
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
		// tgtData.put(ProjectBudget.F__ID, new ObjectId());
		// tgtData.put(ProjectBudget.F_DESC, project.getDesc());
		// tgtData.put(ProjectBudget.F_DESC_EN, project.getDesc_e());
		// tgtData.put(ProjectBudget.F_CHILDREN,
		// srcdata.get(BudgetItem.F_CHILDREN));
		// tgtData.put(ProjectBudget.F_BUDGET_VALUE,
		//					project.getValue("_budget_code"));//$NON-NLS-1$
		//
		// ProjectBudget budget = ModelService.createModelObject(tgtData,
		// ProjectBudget.class);
		// try {
		// budget.doInsert(context);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// BasicBSONList items = (BasicBSONList) project
		//					.getValue("_temp_itemcode");//$NON-NLS-1$
		// if (items != null) {
		// for (Object item : items) {
		// ProductItem productItem = ModelService
		// .createModelObject(ProductItem.class);
		// productItem.setValue(ProductItem.F_PROJECT_ID,
		// project.get_id());
		// productItem.setValue(ProductItem.F_DESC, item.toString());
		// try {
		// productItem.doSave(new CurrentAccountContext());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
		//
		// DBCollection updatecol = getCol(IModelConstants.C_PROJECT);
		// updatecol
		// .update(new BasicDBObject().append(Project.F__ID,
		// project.get_id()), new BasicDBObject().append(
		//							"$set",//$NON-NLS-1$
		// new BasicDBObject().append(Project.F_WORK_ID,
		// root.get_id()).append(Project.F_FOLDER_ID,
		// folderRoot.get_id())));
		// }
		//
		//		col.update(append, new BasicDBObject().append("$unset", //$NON-NLS-1$
		//				new BasicDBObject().append("_temp_itemcode", 1)),//$NON-NLS-1$
		// false, true);
		//		col.update(append, new BasicDBObject().append("$unset", //$NON-NLS-1$
		//				new BasicDBObject().append("_budget_code", 1)), //$NON-NLS-1$
		// false, true);

		// try {
		// SQLResult result = SQLUtil.SQL_QUERY("budget", "select * from jy");
		// if (!result.isEmpty()) {
		// // 循环构造用户
		// Iterator<SQLRow> iter = result.iterator();
		// while (iter.hasNext()) {
		// SQLRow row = iter.next();
		// String projectnumber = (String) row
		// .getValue("projectnumber");
		// Double budgetvalue = (Double) row.getValue("budgetvalue");
		// BasicDBObject o = new BasicDBObject();
		// o.put(Project.F_PROJECT_NUMBER, projectnumber);
		// DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
		// DBObject findOne = projectCol.findOne(o);
		// if (findOne != null) {
		// ObjectId project_id = (ObjectId) findOne
		// .get(Project.F__ID);
		// if (project_id != null) {
		// DBCollection col = getCol(IModelConstants.C_PROJECT_BUDGET);
		// col.update(
		// new BasicDBObject().append(
		// ProjectBudget.F_PROJECT_ID,
		// project_id),
		// new BasicDBObject().append(
		// "$set",
		// new BasicDBObject()
		// .append(ProjectBudget.F_BUDGET_VALUE,
		// budgetvalue)));
		// }
		// }
		// }
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
//		Map<String, Double> map = new HashMap<String, Double>();
//		map.put("TBKF13122", 1256000d);
//		map.put("TBKF14001", 220000d);
//		map.put("TBKF14007", 112000d);
//		map.put("TBKF1400A", 30000d);
//		map.put("TBKF14011", 68000d);
//		map.put("TBKF14031", 50000d);
//		map.put("TBKF14033", 46000d);
//		map.put("TBKF14039", 20000d);
//		map.put("TBKF14045", 80000d);
//		map.put("TBKF14046", 66000d);
//		map.put("TBKF14047", 66000d);
//		map.put("TBKF1404C", 7000d);
//		map.put("TBKF14051", 42000d);
//		map.put("TBKF14053", 102000d);
//		map.put("TBKF14054", 20000d);
//		map.put("TBKF14055", 50000d);
//		map.put("TBKF14057", 46000d);
//		map.put("TBKF14058", 10000d);
//
//		DBCollection projectCol = getCol(IModelConstants.C_PROJECT);
//		DBCollection projectBudgetCol = getCol(IModelConstants.C_PROJECT_BUDGET);
//
//		Set<String> keySet = map.keySet();
//		for (String projectNumber : keySet) {
//			DBObject dbObject = projectCol.findOne(new BasicDBObject().append(
//					Project.F_PROJECT_NUMBER, projectNumber));
//			Project project = ModelService.createModelObject(dbObject,
//					Project.class);
//			ProjectBudget projectBudget = project.getBudget();
//			if(projectBudget != null){
//				projectBudgetCol.update(new BasicDBObject().append(
//						ProjectBudget.F_PROJECT_ID, project.get_id()),
//						new BasicDBObject().append("$set", new BasicDBObject()
//								.append(ProjectBudget.F_BUDGET_VALUE,
//										map.get(projectNumber))));
//			}
//
//		}
		return true;

	}

	@SuppressWarnings("unused")
	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
