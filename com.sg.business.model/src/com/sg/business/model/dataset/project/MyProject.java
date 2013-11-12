package com.sg.business.model.dataset.project;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * 项目导航
 * </p>
 * 继承于{@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}，
 * 用于管理当前用户所负责的项目和参与的项目
 * 
 * @author yangjun
 *
 */
public class MyProject extends SingleDBCollectionDataSetFactory {

	/**
	 * 项目导航构造函数，用于设置项目导航的存放数据库及数据存储表
	 */
	public MyProject() {
		//设置项目导航的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	/**
	 * 获取当前账号负责的项目和参与的项目
	 * 
	 * @return 返回当前账号负责的项目和参与的项目，
	 * 为{@link com.mongodb.DBObject}类型的数据
	 */
	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号
		try {
			String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
			// 查询条件为本人负责的项目和本人参与的项目
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(
					"$or",
					new BasicDBObject[] {
							new BasicDBObject().append(Project.F_CHARGER,
									userId),
							new BasicDBObject().append(Project.F_PARTICIPATE,
									userId) });
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
	}

}
