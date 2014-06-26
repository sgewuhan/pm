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
 * 用于管理当前用户所商务负责的项目
 * 
 * @author yangjun
 * 
 */
public class MyProjectSales extends SingleDBCollectionDataSetFactory {

	private String userId;

	/**
	 * 项目导航构造函数，用于设置项目导航的存放数据库及数据存储表
	 */
	public MyProjectSales() {
		// 设置项目导航的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
	}

	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号
		try {
			// 查询条件为本人负责的项目和本人参与的项目
			DBObject queryCondition = createQueryCondition();
			queryCondition.put(Project.F_BUSINESS_CHARGER, userId);
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null); //$NON-NLS-1$
		}
	}

	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Project.F_ACTUAL_START, -1).append(Project.F_PLAN_START, -1); //$NON-NLS-1$
	}
}
