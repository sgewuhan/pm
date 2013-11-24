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

			int proFinishCount = 0;
			int finishDelayCount = 0;
			int finishNormalCount = 0;

			int proProcessCount = 0;
			int processDelayCount = 0;
			int processNormalCount = 0;
			
			
			int advanceCount=0;

			Date startDate = getStartDate();
			Date endDate = getEndDate();
			DBCursor cur = col.find(getQueryCondtion(startDate, endDate));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				Project project = ModelService.createModelObject(dbo,
						Project.class);
				if (ILifecycle.STATUS_FINIHED_VALUE.equals(project
						.getLifecycleStatus())) {
					proFinishCount++;
					if (project.isDelay()) {
						finishDelayCount++;
					} else {
						finishNormalCount++;
					}
					if(project.isAdvanced()){
						advanceCount++;
					}
				} else if (ILifecycle.STATUS_WIP_VALUE.equals(project
						.getLifecycleStatus())) {
					proProcessCount++;
					if (project.maybeDelay()) {
						processDelayCount++;
					} else {
						processNormalCount++;
					}
					if(project.maybeAdvanced()){
						advanceCount++;
					}
				}
				result.add(project);
			}
			map.put(F_SUMMARY_TOTAL, result.size());
			
			map.put(F_SUMMARY_FINISHED, proFinishCount);
			map.put(F_SUMMARY_FINISHED_DELAY, finishDelayCount);
			map.put(F_SUMMARY_FINISHED_NORMAL, finishNormalCount);
			
			map.put(F_SUMMARY_PROCESSING, proProcessCount);
			map.put(F_SUMMARY_PROCESSING_DELAY, processDelayCount);
			map.put(F_SUMMARY_PROCESSING_NORMAL, processNormalCount);
			
			map.put(F_SUMMARY_ADVANCE, advanceCount);
			map.put(F_SUMMARY_DELAY, finishDelayCount+processDelayCount);
			map.put(F_SUMMARY_NORMAL, finishNormalCount+processNormalCount);

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

	// public void setF_SUMMARY_NORMAL_PROCESS(int count) {
	// setValue(F_SUMMARY_NORMAL_PROCESS,count);
	// }
	//
	// public void setF_SUMMARY_DELAY(int count) {
	// setValue(F_SUMMARY_DELAY,count);
	// }
	//
	// public void setF_SUMMARY_ADVANCE(int count) {
	// setValue(F_SUMMARY_ADVANCE,count);
	// }
	//
	// public void setF_SUMMARY_NORMAL_COST(int count) {
	// setValue(F_SUMMARY_NORMAL_COST,count);
	// }
	//
	// public void setF_SUMMARY_OVER_COST(int count) {
	// setValue(F_SUMMARY_OVER_COST,count);
	// }
	//
	// @Override
	// public void setF_SUMMARY_TOTAL(int count) {
	// setValue(F_SUMMARY_TOTAL,count);
	// }
	//
	// @Override
	// public void setF_SUMMARY_FINISHED(int count) {
	// setValue(F_SUMMARY_FINISHED,count);
	// }
	//
	// @Override
	// public void setF_SUMMARY_PROCESSING(int count) {
	// setValue(F_SUMMARY_PROCESSING,count);
	// }

}
