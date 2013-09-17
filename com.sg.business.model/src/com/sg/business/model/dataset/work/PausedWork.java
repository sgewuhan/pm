package com.sg.business.model.dataset.work;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;

public class PausedWork extends SingleDBCollectionDataSetFactory {

	public PausedWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	/**
	 * 获取当前账号负责的工作和参与的工作
	 * 
	 * @return 返回当前账号负责的工作和参与的工作， 为{@link com.mongodb.DBObject}类型的数据
	 */
	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号
		try {
			AccountInfo account = UserSessionContext.getAccountInfo();
			String userid = account.getConsignerId();
			// 查询本人参与的工作
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(Work.F_PARTICIPATE, userid);
			// 生命周期状态为已经暂停
			queryCondition.put(Work.F_LIFECYCLE, Work.STATUS_PAUSED_VALUE);
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append(Work.F__ID, null);
		}
	}

	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Work.F__ID, -1);
	}

}
