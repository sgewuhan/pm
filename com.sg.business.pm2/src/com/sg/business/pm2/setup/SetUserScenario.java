package com.sg.business.pm2.setup;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.business.model.User;

public class SetUserScenario implements ISchedualJobRunnable {

	public SetUserScenario() {
	}

	@Override
	public boolean run() throws Exception {
		DBCollection col = getCol(IModelConstants.C_USER);
		DBCursor cursor = col.find();
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			User user = ModelService.createModelObject(object, User.class);
			List<String> scenario = new ArrayList<String>();
			if (Boolean.TRUE.equals(user.getValue(User.F_IS_ADMIN))) {
				scenario.add("projectcharger");
				scenario.add("businessmanager");
				scenario.add("projectmanagement");
				scenario.add("visualization");
				scenario.add("resourcemanagement");
				scenario.add("systemmanager");
			} else {
				scenario.add("projectcharger");
				// 添加业务管理员
				setUserScenarios(scenario, user,
						new String[] { "businessmanager" },
						Role.ROLE_BUSINESS_ADMIN_ID);
				// 添加项目管理员
				setUserScenarios(scenario, user,
						new String[] { "projectmanagement" },
						Role.ROLE_PROJECT_ADMIN_ID);
				// 添加管理者
				setUserScenarios(scenario, user, new String[] {
						"visualization", "resourcemanagement", },
						Role.ROLE_DEPT_MANAGER_ID);
			}
			col.update(new BasicDBObject().append(User.F__ID, user.get_id()),
					new BasicDBObject().append("$set", new BasicDBObject()
							.append(User.F_SCENARIO, scenario)));
		}
		return true;
	}

	private void setUserScenarios(List<String> scenarios, User user,
			String[] scenarioList, String role) {
		List<PrimaryObject> roles = user.getRoles(role);
		if (roles.size() > 0) {
			for (String scenario : scenarioList) {
				scenarios.add(scenario);
			}
		}
	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
