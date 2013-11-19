package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class OrganizationProjectProvider extends ProjectProvider {

	private Organization organization;

	public void setOrganization(Organization org) {
		this.organization = org;
	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		DBCollection col = getCollection(IModelConstants.C_PROJECT);
		DBCursor cur = col.find(new BasicDBObject().append(Project.F_LAUNCH_ORGANIZATION,
				this.organization.get_id()).append(
				Project.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE })));
		List<PrimaryObject> result=new ArrayList<PrimaryObject>();
		while(cur.hasNext()){
			DBObject dbo = cur.next();
			result.add(ModelService.createModelObject(dbo, Project.class));
		}
		return result;
	}

}
