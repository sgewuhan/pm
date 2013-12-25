package com.sg.business.model.condition;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IRelationConditionProvider;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.User;

public class OrgOfProjectManagerCondition implements IRelationConditionProvider {

	public OrgOfProjectManagerCondition() {
	}

	@Override
	public DBObject getCondition(PrimaryObject primaryObject) {
		List<String> list = getProjectIdList(primaryObject);
		return new BasicDBObject().append(User.F_USER_ID,
				new BasicDBObject().append("$in", list)); //$NON-NLS-1$
	}

	private List<String> getProjectIdList(PrimaryObject primaryObject) {
		List<String> list = new ArrayList<String>();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		DBCursor cur = col.find(new BasicDBObject().append(
				getOrgFieldName(), primaryObject.get_id()).append(
				ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE })));

		while (cur.hasNext()) {
			DBObject next = cur.next();
			Project project = ModelService.createModelObject(next,
					Project.class);
			list.add(project.getChargerId());
		}
		return list;
	}

	protected String getOrgFieldName() {
		return Project.F_LAUNCH_ORGANIZATION;
	}

}
