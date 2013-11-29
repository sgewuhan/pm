package com.sg.business.project.setup;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectDelete implements Runnable {
	private static final ObjectId[] DELETELIST = new ObjectId[] {
			new ObjectId("5292c180e0cc84d8d5965ae9"),
			new ObjectId("5292c90fe0cc84d8d5965c88"),
			new ObjectId("529550cdca7864f59ad1472d"),
			new ObjectId("52955140ca7864f59ad14867"),
			new ObjectId("52955262ca7864f59ad1499a") };

	public ProjectDelete() {
	}

	@Override
	public void run() {
		DBCollection col = getCol();
		for (ObjectId _id : DELETELIST) {
			Project project = ModelService
					.createModelObject(Project.class, _id);
			try {
				project.doRemove(new CurrentAccountContext());
			} catch (Exception e) {
			}
			col.remove(new BasicDBObject().append(Project.F__ID,_id));
		}
	}

	private DBCollection getCol() {
		return DBActivator.getCollection(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

}
