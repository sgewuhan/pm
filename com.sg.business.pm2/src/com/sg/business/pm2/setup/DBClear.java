package com.sg.business.pm2.setup;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;

public class DBClear implements Runnable {
//	private static final ObjectId[] NOTDELETE = new ObjectId[] { null,
//			new ObjectId("5281cb76e0cc49249f3c6715"),
//			new ObjectId("5284340fe0cc7c1c547d44fa"),
//			new ObjectId("5281d483e0cc49249f3c68e7"),
//			new ObjectId("528476a0e0cc7c1c547d58d5"),
//			new ObjectId("52844a07e0cc7c1c547d4a82"),
//			new ObjectId("5282dac9e0ccf8afc27a1a3e") };

	@Override
	public void run() {
		 clearData();

	}


	// private void clearData() {
	// // 清除公告牌
	// DBCollection col = getCol(IModelConstants.C_BULLETINBOARD);
	// col.drop();
	//
	// // 清楚项目的日历牌设置
	// col = getCol(IModelConstants.C_CALENDAR_SETTING);
	// col.remove(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除公司关联的工作令号
	// col = getCol(IModelConstants.C_COMPANY_WORKORDER);
	// col.drop();
	//
	// // 清除交付物
	// col = getCol(IModelConstants.C_DELIEVERABLE);
	// col.remove(new BasicDBObject().append(Deliverable.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除文档
	// col = getCol(IModelConstants.C_DOCUMENT);
	// col.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除目录
	// col = getCol(IModelConstants.C_FOLDER);
	// col.remove(new BasicDBObject().append(Folder.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // // 清除除了文档模板以外的编号
	// // // col = getCol(IModelConstants.C__IDS);
	// // // col.remove(new BasicDBObject().append("name",
	// // // new BasicDBObject().append("$ne", "documenttemplatenumber")));
	//
	// // 清除log
	// col = getCol("log");
	// col.drop();
	//
	// // 清除消息
	// col = getCol(IModelConstants.C_MESSAGE);
	// col.drop();
	//
	// // 清除项目
	// col = getCol(IModelConstants.C_PROJECT);
	// col.remove(new BasicDBObject().append(Project.F__ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除项目预算
	// col = getCol(IModelConstants.C_PROJECT_BUDGET);
	// col.remove(new BasicDBObject().append(ProjectBudget.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除项目角色
	// col = getCol(IModelConstants.C_PROJECT_ROLE);
	// col.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除项目角色指派
	// col = getCol(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
	// col.remove(new BasicDBObject().append(ProjectRoleAssignment.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除研发成本
	// col = getCol(IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
	// col.drop();
	//
	// // 清除研发成本分摊
	// col = getCol(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
	// col.drop();
	//
	// // 清除work
	// col = getCol(IModelConstants.C_WORK);
	// col.remove(new BasicDBObject().append(Work.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除work前后置
	// col = getCol(IModelConstants.C_WORK_CONNECTION);
	// col.remove(new BasicDBObject().append(WorkConnection.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除工时分配
	// col = getCol(IModelConstants.C_WORKS_ALLOCATE);
	// col.remove(new BasicDBObject().append(WorksAllocate.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // 清除实际工时
	// col = getCol(IModelConstants.C_WORKS_PERFORMENCE);
	// col.remove(new BasicDBObject().append(WorksPerformence.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// col = getCol(IModelConstants.C_WORK);
	// DBCursor cursor = col.find();
	// List<ObjectId> workIds = new ArrayList<ObjectId>();
	// while(cursor.hasNext()){
	// DBObject object = cursor.next();
	// ObjectId _id = (ObjectId) object.get(Work.F__ID);
	// workIds.add(_id);
	// }
	//
	// // 清除用户任务
	// col = getCol(IModelConstants.C_USERTASK);
	// col.remove(new BasicDBObject().append(UserTask.F_WORK_ID,
	// new BasicDBObject().append("$nin", workIds)));
	//
	// // 清除用户的最近打开
	// col = getCol("account");
	// col.update(
	// new BasicDBObject(),
	// new BasicDBObject().append("$unset",
	// new BasicDBObject().append("lastopened", "")), false,
	// true);
	// }

	private void clearData() {
		// 清除公告牌
		DBCollection col = getCol(IModelConstants.C_BULLETINBOARD);
		col.drop();

		// 清楚项目的日历牌设置
		col = getCol(IModelConstants.C_CALENDAR_SETTING);
		col.remove(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID,
				new BasicDBObject().append("$ne", null)));

		// 清除公司关联的工作令号
		col = getCol(IModelConstants.C_COMPANY_WORKORDER);
		col.drop();

		// 清除交付物
		col = getCol(IModelConstants.C_DELIEVERABLE);
		col.drop();

		// 清除文档
		col = getCol(IModelConstants.C_DOCUMENT);
		col.drop();

		// 清除目录
		col = getCol(IModelConstants.C_FOLDER);
		col.remove(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID,
				new BasicDBObject().append("$ne", null)));

		// 清除除了文档模板以外的编号
		col = getCol(IModelConstants.C__IDS);
		col.remove(new BasicDBObject().append("name",
				new BasicDBObject().append("$ne", "documenttemplatenumber")));

		// 清除log
		col = getCol("log");
		col.drop();

		// 清除消息
		col = getCol(IModelConstants.C_MESSAGE);
		col.drop();

		// 清除项目
		col = getCol(IModelConstants.C_PROJECT);
		col.drop();

		// 清除项目预算
		col = getCol(IModelConstants.C_PROJECT_BUDGET);
		col.drop();

		// 清除项目角色
		col = getCol(IModelConstants.C_PROJECT_ROLE);
		col.drop();

		// 清除项目角色指派
		col = getCol(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		col.drop();

		// 清除研发成本
		col = getCol(IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		col.drop();

		// 清除研发成本分摊
		col = getCol(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		col.drop();

		// 清除work
		col = getCol(IModelConstants.C_WORK);
		col.drop();

		// 清除work前后置
		col = getCol(IModelConstants.C_WORK_CONNECTION);
		col.drop();

		// 清除工时分配
		col = getCol(IModelConstants.C_WORKS_ALLOCATE);
		col.drop();

		// 清除实际工时
		col = getCol(IModelConstants.C_WORKS_PERFORMENCE);
		col.drop();

		// 清除用户任务
		col = getCol(IModelConstants.C_USERTASK);
		col.drop();

		// 清除用户的最近打开
		col = getCol("account");
		col.update(
				new BasicDBObject(),
				new BasicDBObject().append("$unset",
						new BasicDBObject().append("lastopened", "")), false,
				true);
	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
