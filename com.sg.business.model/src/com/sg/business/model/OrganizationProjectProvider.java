package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mobnut.commons.util.file.FileUtil;
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
	private DBCollection col;

	public void setOrganization(Organization org) {
		this.organization = org;
		setValue(F__ID, org.get_id());
		setValue(F_DESC, org.getDesc());
		col = getCollection(IModelConstants.C_PROJECT);
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

			Map<String, Object> map = new HashMap<String, Object>();

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
			DBCursor cur = col.find(getQueryCondtion(startDate, endDate));
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
			map.put(F_SUMMARY_TOTAL, result.size());

			map.put(F_SUMMARY_FINISHED, iF_SUMMARY_FINISHED);
			map.put(F_SUMMARY_FINISHED_DELAY, iF_SUMMARY_FINISHED_DELAY);
			map.put(F_SUMMARY_FINISHED_NORMAL, iF_SUMMARY_FINISHED_NORMAL);
			map.put(F_SUMMARY_FINISHED_ADVANCE, iF_SUMMARY_FINISHED_ADVANCED);

			map.put(F_SUMMARY_PROCESSING, iF_SUMMARY_PROCESSING);
			map.put(F_SUMMARY_PROCESSING_DELAY, iF_SUMMARY_PROCESSING_DELAY);
			map.put(F_SUMMARY_PROCESSING_NORMAL, iF_SUMMARY_PROCESSING_NORMAL);
			map.put(F_SUMMARY_PROCESSING_ADVANCE, iF_SUMMARY_PROCESSING_ADVANCE);

			setSummaryDate(map);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
		return result;
	}

	@Override
	public String getProjectSetName() {
		return getDesc() + "项目集";
	}

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
