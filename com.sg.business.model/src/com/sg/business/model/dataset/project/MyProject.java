package com.sg.business.model.dataset.project;

import org.eclipse.swt.SWT;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;

public class MyProject extends SingleDBCollectionDataSetFactory {

	public MyProject() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号
		try {
			AccountInfo account = UserSessionContext.getAccountInfo();
			String userid = account.getconsignerId();
			// 查询条件为本人负责的项目和本人参与的项目
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(
					"$or",
					new BasicDBObject[] {
							new BasicDBObject().append(Project.F_CHARGER,
									userid),
							new BasicDBObject().append(Project.F_PARTICIPATE,
									userid) });
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e.getMessage(), SWT.ICON_ERROR);
			return new BasicDBObject().append("_id", null);
		}
	}

}
