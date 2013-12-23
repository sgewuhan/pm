package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;

public class OrganizationProjectProvider extends ProjectProvider {

	private Organization organization;
	private DBCollection projectCol;
	private DBCollection orgCol;
	DBCollection usercol;

	public void setOrganization(Organization org) {
		this.organization = org;
		setValue(F__ID, org.get_id());
		setValue(F_DESC, org.getDesc());
		projectCol = getCollection(IModelConstants.C_PROJECT);
		orgCol = getCollection(IModelConstants.C_ORGANIZATION);
		usercol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
	}

	@Override
	public String getTypeName() {
		return "组织项目集";
	}

	@Override
	public String getProjectSetCoverImage() {
		return FileUtil.getImageURL("project_72.png",
				"com.sg.business.project", BusinessResource.IMAGE_FOLDER);
	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {
			sum.clean();
			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCursor cur = projectCol
					.find(getQueryCondtion(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				ProjectPresentation pres = project.getPresentation();
				pres.loadSummary(sum);
				result.add(project);
			}
			sum.total = result.size();
			sum.subOrganizationProjectProvider = getSubOrganizationProvider();
			sum.subChargerProjectProvider = getSubUserProvider(organization);

		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
		return result;
	}

	public List<ProjectProvider> getSubOrganizationProvider() {
		List<ProjectProvider> list = new ArrayList<ProjectProvider>();

		DBCursor cur = orgCol.find(new BasicDBObject().append(
				Organization.F_PARENT_ID, organization.get_id()).append(
				Organization.F__ID,
				new BasicDBObject().append("$in", getAviableOrganizationId())));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Organization org = ModelService.createModelObject(dbo,
					Organization.class);
			ProjectProvider pp = org.getAdapter(ProjectProvider.class);
			list.add(pp);
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object[] getAviableOrganizationId() {
		Set<ObjectId> set = new HashSet<ObjectId>();

		List prjOrgList = projectCol.distinct(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append(
						ILifecycle.F_LIFECYCLE,
						new BasicDBObject().append("$in", new String[] {
								ILifecycle.STATUS_FINIHED_VALUE,
								ILifecycle.STATUS_WIP_VALUE })));
		set.addAll(prjOrgList);

		List parentOrgList = orgCol.distinct(Organization.F_PARENT_ID,
				new BasicDBObject().append(Organization.F__ID,
						new BasicDBObject().append("$in", prjOrgList)));
		while (parentOrgList != null && !parentOrgList.isEmpty()
				|| (parentOrgList.size() == 1 && parentOrgList.get(0) != null)) {
			set.addAll(parentOrgList);
			parentOrgList = orgCol.distinct(Organization.F_PARENT_ID,
					new BasicDBObject().append(Organization.F__ID,
							new BasicDBObject().append("$in", parentOrgList)));
		}
		return set.toArray(new Object[0]);
	}

	public List<ProjectProvider> getSubUserProvider(PrimaryObject po) {
		List<ProjectProvider> list = new ArrayList<ProjectProvider>();
		DBCursor cur = usercol.find(new BasicDBObject().append(User.F_USER_ID,
				new BasicDBObject().append("$in", getAviableUser(po))));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			User user = ModelService.createModelObject(dbo, User.class);
			ProjectProvider pp = user.getAdapter(ProjectProvider.class);
			list.add(pp);
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getAviableUser(PrimaryObject po) {
		Set<ObjectId> set = new HashSet<ObjectId>();
		List prjManagerList = projectCol.distinct(
				Project.F_CHARGER,
				new BasicDBObject().append(
						ILifecycle.F_LIFECYCLE,
						new BasicDBObject().append("$in", new String[] {
								ILifecycle.STATUS_FINIHED_VALUE,
								ILifecycle.STATUS_WIP_VALUE })).append(
						Project.F_LAUNCH_ORGANIZATION, po.get_id()));
		set.addAll(prjManagerList);
		return set.toArray(new Object[0]);
	}

	@Override
	public String getProjectSetName() {
		return getDesc();
	}

	/**
	 * 项目集的查询条件
	 * 
	 * @param start
	 * @param stop
	 * @return
	 */
	protected BasicDBObject getQueryCondtion(Date start, Date stop) {
		return super.getQueryCondtion(start, stop).append(
				Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in",
						getOrganizations(organization)));
	}

	private List<?> getOrganizations(Organization org) {
		List<Object> result = new ArrayList<Object>();
		result.add(org.get_id());
		List<PrimaryObject> children = org.getChildrenOrganization();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				result.addAll(getOrganizations((Organization) children.get(i)));
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((organization == null) ? 0 : organization.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrganizationProjectProvider other = (OrganizationProjectProvider) obj;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		return true;
	}

	public Organization getOrganization() {
		return organization;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ObjectId> getAllProjectId() {
		BasicDBObject query = new BasicDBObject();
		query.put(Project.F_LAUNCH_ORGANIZATION, new BasicDBObject().append(
				"$in", getOrganizations(organization)));
		return projectCol.distinct(Project.F__ID, query);
	}
}
