package com.sg.business.pm2.setup;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;

public class DBClear implements Runnable {

	@Override
	public void run() {

		clearData();
		

	}

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
