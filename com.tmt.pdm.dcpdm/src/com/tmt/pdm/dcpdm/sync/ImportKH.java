package com.tmt.pdm.dcpdm.sync;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;

public class ImportKH implements Runnable {

	@Override
	public void run() {
		updateProjectLaunchOrg();
	}

	private void updateProjectLaunchOrg() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		DBCursor cur = col.find();
		while (cur.hasNext()) {
			DBObject next = cur.next();
			Object org = next.get(Project.F_LAUNCH_ORGANIZATION);
			if (org instanceof ObjectId) {
				col.update(new BasicDBObject().append(Project.F__ID,
						next.get(Project.F__ID)), new BasicDBObject().append(
						"$set", new BasicDBObject().append( //$NON-NLS-1$
								Project.F_LAUNCH_ORGANIZATION,
								new Object[] { org })));
			}
		}
	}

}
