package com.sg.business.model.etl;

import java.util.Calendar;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;

public class ProjectETLJob implements ISchedualJobRunnable {

	private DBCollection pjCol;

	public ProjectETLJob() {
		pjCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	public boolean run() throws Exception {
		Commons.loginfo("Project data ETL Starting...");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long now = cal.getTimeInMillis();

		BasicDBObject query = new BasicDBObject();
		query.put(Project.F_LIFECYCLE,
				new BasicDBObject()
						.append("$in", new String[] {
								Project.STATUS_CANCELED_VALUE,
								Project.STATUS_FINIHED_VALUE,
								Project.STATUS_PAUSED_VALUE,
								Project.STATUS_WIP_VALUE }));
		query.put(
				"$or",
				new Object[] {
						new BasicDBObject().append(ProjectETL.F_ETL + "."
								+ ProjectETL.F_TRANSFERDATE, null),
						new BasicDBObject().append(ProjectETL.F_ETL + "."
								+ ProjectETL.F_TRANSFERDATE,
								new BasicDBObject().append("$lt", now)) });
		DBCursor cur = pjCol.find(query);
		while (cur.hasNext()) {
			DBObject dbObject = (DBObject) cur.next();
			ObjectId id = (ObjectId) dbObject.get(Project.F__ID);
			if (id != null) {
				Project project = ModelService.createModelObject(Project.class,
						id);
				ProjectETL pres = project.getETL();
				pres.doETL();
				if (Portal.getDefault().isDevelopMode()) {
					Commons.loginfo(project.getLabel() + " ETL finished.");
				}
			}
		}
		Commons.loginfo("Project data ETL finished.");
		return true;
	}

}
