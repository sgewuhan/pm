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
			int iF_SUMMARY_FINISHED = 0;
			int iF_SUMMARY_FINISHED_DELAY = 0;
			int iF_SUMMARY_FINISHED_NORMAL = 0;
			int iF_SUMMARY_FINISHED_ADVANCED = 0;

			int iF_SUMMARY_PROCESSING = 0;
			int iF_SUMMARY_PROCESSING_DELAY = 0;
			int iF_SUMMARY_PROCESSING_NORMAL = 0;
			int iF_SUMMARY_PROCESSING_ADVANCE = 0;

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCursor cur = projectCol
					.find(getQueryCondtion(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				if (ILifecycle.STATUS_FINIHED_VALUE.equals(project
						.getLifecycleStatus())) {
					iF_SUMMARY_FINISHED++;
					if (project.isDelay()) {
						iF_SUMMARY_FINISHED_DELAY++;
					} else if (project.isAdvanced()) {
						iF_SUMMARY_FINISHED_ADVANCED++;
					} else {
						iF_SUMMARY_FINISHED_NORMAL++;
					}
				} else if (ILifecycle.STATUS_WIP_VALUE.equals(project
						.getLifecycleStatus())) {
					iF_SUMMARY_PROCESSING++;
					if (project.maybeDelay()) {
						iF_SUMMARY_PROCESSING_DELAY++;
					} else if (project.maybeAdvanced()) {
						iF_SUMMARY_PROCESSING_ADVANCE++;
					} else {
						iF_SUMMARY_PROCESSING_NORMAL++;
					}
				}
				result.add(project);
			}
			summaryData.total = result.size();
			
			summaryData.finished=iF_SUMMARY_FINISHED;
			summaryData.finished_delay=iF_SUMMARY_FINISHED_DELAY;
			summaryData.finished_normal=iF_SUMMARY_FINISHED_NORMAL;
			summaryData.finished_advance=iF_SUMMARY_FINISHED_ADVANCED;

			summaryData.processing=iF_SUMMARY_PROCESSING;
			summaryData.processing_delay=iF_SUMMARY_PROCESSING_DELAY;
			summaryData.processing_normal=iF_SUMMARY_PROCESSING_NORMAL;
			summaryData.processing_advance=iF_SUMMARY_PROCESSING_ADVANCE;
			
     		summaryData.subOrganizationProjectProvider=getSubOrganizationProvider();
			summaryData.subChargerProjectProvider=getSubUserProvider(organization);
			
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return result;
	}

	public List<ProjectProvider> getSubOrganizationProvider() {
		List<ProjectProvider> list = new ArrayList<ProjectProvider>();

		DBCursor cur = orgCol.find( new BasicDBObject().append(
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
			User user = ModelService.createModelObject(dbo,
					User.class);
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
		return getDesc() + "项目集";
	}

	/**
	 * 项目集的查询条件
	 * 
	 * @param start
	 * @param stop
	 * @return
	 */
	private DBObject getQueryCondtion(Date start, Date stop) {

		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_LAUNCH_ORGANIZATION, new BasicDBObject().append(
				"$in", getOrganizations(organization)));
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));
		dbo.put("$or",
				new BasicDBObject[] {

						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Project.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Project.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(
								"$and",
								new BasicDBObject[] {
										new BasicDBObject().append(
												Project.F_ACTUAL_START,
												new BasicDBObject().append(
														"$lte", start)),
										new BasicDBObject().append(
												Project.F_ACTUAL_FINISH,
												new BasicDBObject().append(
														"$gte", stop)) }) });

		return dbo;
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

}
