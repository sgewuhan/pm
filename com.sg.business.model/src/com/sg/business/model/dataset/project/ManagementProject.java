package com.sg.business.model.dataset.project;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;

public class ManagementProject extends SingleDBCollectionDataSetFactory {

	public ManagementProject() {
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	@Override
	public DBObject getQueryCondition() {
		// 获得当前帐号可管理的项目职能组织
		try {
			AccountInfo account = UserSessionContext.getAccountInfo();
			String userId = account.getconsignerId();
			User user = User.getUserById(userId);
			List<PrimaryObject> orglist = user
					.getRoleGrantedOrganization(Role.ROLE_BUSINESS_ADMIN_ID);
			ObjectId[] ids = new ObjectId[orglist.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = orglist.get(i).get_id();
			}
			BasicDBObject condition = new BasicDBObject();
			condition.put(Project.F_FUNCTION_ORGANIZATION,
					new BasicDBObject().append("$in", ids));
			return condition;
		} catch (Exception e) {
			MessageUtil.showToast(e.getMessage(), SWT.ICON_ERROR);
			return new BasicDBObject().append("_id", null);
		}
	}

}
