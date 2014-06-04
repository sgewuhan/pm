package com.sg.business.organization.job;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectMonthData;

public class DeleteAdminUser implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		DBCollection ProjectCol = getCol(IModelConstants.C_PROJECT);
		DBCursor projectCursor = ProjectCol.find(new BasicDBObject().append(
				Project.F_PARTICIPATE,
				new BasicDBObject().append("$in", new String[] { "yangjun",
						"zhonghua", "jinxt"

				})));
		while (projectCursor.hasNext()) {
			DBObject dbObject = projectCursor.next();
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
			ProjectCol
					.update(new BasicDBObject().append(Project.F__ID,
							project.get_id()), new BasicDBObject().append(
							"$set", new BasicDBObject().append(
									Project.F_PARTICIPATE, list)), false, false);
		}
		DBCollection projectMonthDataCol = getCol(IModelConstants.C_PROJECT_MONTH_DATA);
		DBCursor projectMonthDataCursor = projectMonthDataCol.find();
		while (projectMonthDataCursor.hasNext()) {
			DBObject dbObject = projectMonthDataCursor.next();
			ProjectMonthData projectMonthData = ModelService.createModelObject(
					dbObject, ProjectMonthData.class);
			ObjectId project_id = (ObjectId) projectMonthData
					.getValue(ProjectMonthData.F_PROJECTID);
			ObjectId projectMonthDataId = projectMonthData.get_id();
			Integer month = projectMonthData.getMonth();
			Integer year = projectMonthData.getYear();
			System.out.println("" + year + " " + month + " " + project_id);
			projectMonthDataCol.remove(new BasicDBObject()
					.append(ProjectMonthData.F_MONTH, month)
					.append(ProjectMonthData.F_YEAR, year)
					.append(ProjectMonthData.F_PROJECTID, project_id)
					.append(ProjectMonthData.F__ID,
							new BasicDBObject().append("$ne",
									projectMonthDataId)));

		}
		return true;
	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
