package com.sg.business.organization.job;

import org.bson.types.BasicBSONList;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;

public class DeleteAdminUser implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		DBCollection col = getCol(IModelConstants.C_PROJECT);
		DBCursor cursor = col.find(new BasicDBObject().append(
				Project.F_PARTICIPATE,
				new BasicDBObject().append("$in", new String[] { "yangjun",
						"zhonghua", "jinxt"

				})));
		while (cursor.hasNext()) {
			DBObject dbObject = cursor.next();
			Project project = ModelService.createModelObject(dbObject,
					Project.class);
			BasicBSONList list = (BasicBSONList) project
					.getParticipatesIdList();
			String chargerId = project.getChargerId();
			if (!"yangjun".equals(chargerId)) {
				list.remove("yangjun");
			}
			if (!"zhonghua".equals(chargerId)) {
				list.remove("zhonghua");
			}
			if (!"jinxt".equals(chargerId)) {
				list.remove("jinxt");
			}
			col.update(new BasicDBObject().append(Project.F__ID,
					project.get_id()), new BasicDBObject().append("$set",
					new BasicDBObject().append(Project.F_PARTICIPATE, list)),
					false, false);
		}

		return true;
	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
