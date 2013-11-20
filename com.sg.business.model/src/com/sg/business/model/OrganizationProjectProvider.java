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
	private DBCollection col;

	public void setOrganization(Organization org) {
		this.organization = org;
		setValue(F__ID, org.get_id());
		setValue(F_DESC, org.getDesc());
		col = getCollection(IModelConstants.C_PROJECT);
	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		List<PrimaryObject> projectSet = getProjectSet(organization, result);
		// TODO
		// ¸½¼Ó×Ö¶Î _internal_org_id,_internal_org_desc
		return projectSet;
	}

	private List<PrimaryObject> getProjectSet(PrimaryObject po,
			List<PrimaryObject> result) {

		DBCursor cur = col.find(new BasicDBObject().append(
				Project.F_LAUNCH_ORGANIZATION, po.get_id()).append(
				Project.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE })));

		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Project project = ModelService
					.createModelObject(dbo, Project.class);

			result.add(project);
		}

		List<PrimaryObject> childrenOrganization = ((Organization) po)
				.getChildrenOrganization();
		for (PrimaryObject orgpo : childrenOrganization) {
			getProjectSet(orgpo, result);
		}

		return result;
	}

}
