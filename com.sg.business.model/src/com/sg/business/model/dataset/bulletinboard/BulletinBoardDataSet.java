package com.sg.business.model.dataset.bulletinboard;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.widgets.MessageUtil;

public class BulletinBoardDataSet extends SingleDBCollectionDataSetFactory {

	public BulletinBoardDataSet() {
		super(IModelConstants.DB, IModelConstants.C_BULLETINBOARD);
	}

	@Override
	public DBObject getQueryCondition() {
		try {
			List<ObjectId> orgIds = new ArrayList<ObjectId>();
			String userid = UserSessionContext.getAccountInfo()
					.getconsignerId();
			User user = User.getUserById(userid);
			Organization org = user.getOrganization();
			searchDown(org, orgIds);
			searchUp(org, orgIds);

			BasicDBObject condition = new BasicDBObject();
			condition.put(BulletinBoard.F_ORGANIZATION_ID,
					new BasicDBObject().append("$in", orgIds));
			condition.put(BulletinBoard.F_PARENT_BULLETIN,null);
			return condition;
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
	}

	private void searchDown(Organization org, List<ObjectId> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			if (child.isFunctionDepartment()) {
				list.add(child.get_id());
			}
			searchDown(child, list);
		}
	}

	private void searchUp(Organization org, List<ObjectId> list) {
		list.add(0, org.get_id());
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(BulletinBoard.F_PUBLISH_DATE, -1);
	}
}
