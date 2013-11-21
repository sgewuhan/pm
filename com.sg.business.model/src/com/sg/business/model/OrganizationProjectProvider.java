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
		List<PrimaryObject> projectSet = null;
		try {
			projectSet = getProjectSet(organization, result);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		// TODO 完成查询后，需要写入合计值
		// 参考ProjectProvider的F开头的字段的注释，将值写入
		// 使用ProjectProvider.setSummaryDate(data)方法
		// data是合计值的Map<String,Object> Map的key 为ProjectProvider的F开头的字段
		
		Map<String,Object> map=new HashMap<String,Object>();
		int proTotalCoun=projectSet.size();
		int proFinishCount=0;
		int proProcessCount=0;
		
		for(PrimaryObject po:projectSet){
			if(ILifecycle.STATUS_FINIHED_VALUE.equals(((ILifecycle)po).getLifecycleStatus())){
				proFinishCount++;
			}else if(ILifecycle.STATUS_WIP_VALUE.equals(((ILifecycle)po).getLifecycleStatus())){
				proProcessCount++;
			}
		}
		
		map.put(F_SUMMARY_TOTAL,proTotalCoun);
		map.put(F_SUMMARY_FINISHED,proFinishCount);
		map.put(F_SUMMARY_PROCESSING,proProcessCount);
		
		setSummaryDate(map);
		
		return projectSet;
	}

	private List<PrimaryObject> getProjectSet(Organization organization,
			List<PrimaryObject> result) throws Exception {
		// TODO 获取传入的条件，修改现有代码使之能根据条件进行查询
		// 使用getStartDate()获得开始时间，使用getFinishDate()获得完成时间
		// 错误抛出方法外

		Date startDate = getStartDate();
		Date endDate = getEndDate();
		DBCursor cur = col.find(getQueryCondtion(organization, startDate,
				endDate));
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Project project = ModelService
					.createModelObject(dbo, Project.class);
			result.add(project);
		}
		List<PrimaryObject> childrenOrganization = (organization)
				.getChildrenOrganization();
		for (PrimaryObject orgpo : childrenOrganization) {
			getProjectSet((Organization) orgpo, result);
		}
		return result;
	}

	@Override
	public String getProjectSetName() {
		return getDesc() + "项目集";
	}

	private DBObject getQueryCondtion(Organization organization, Date start,
			Date stop) {

		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_LAUNCH_ORGANIZATION, organization.get_id());
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));
		dbo.put("$or",
				new BasicDBObject[] {
						new BasicDBObject().append(Project.F_PLAN_START,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_PLAN_START,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_ACTUAL_START,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_PLAN_FINISH,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start))
								.append(Project.F_ACTUAL_FINISH,
										new BasicDBObject()
												.append("$lte", stop)),
						new BasicDBObject().append(Project.F_ACTUAL_START,
								new BasicDBObject().append("$lte", start))
								.append(Project.F_ACTUAL_START,
										new BasicDBObject()
												.append("$gte", stop)) });

		return dbo;
	}



//	public  void setF_SUMMARY_NORMAL_PROCESS(int count) {
//		setValue(F_SUMMARY_NORMAL_PROCESS,count);
//	}
//
//	public  void setF_SUMMARY_DELAY(int count) {
//		setValue(F_SUMMARY_DELAY,count);
//	}
//
//	public  void setF_SUMMARY_ADVANCE(int count) {
//		setValue(F_SUMMARY_ADVANCE,count);
//	}
//
//	public  void setF_SUMMARY_NORMAL_COST(int count) {
//		setValue(F_SUMMARY_NORMAL_COST,count);
//	}
//
//	public  void setF_SUMMARY_OVER_COST(int count) {
//		setValue(F_SUMMARY_OVER_COST,count);
//	}
//
//	@Override
//	public void setF_SUMMARY_TOTAL(int count) {
//		setValue(F_SUMMARY_TOTAL,count);
//	}
//
//	@Override
//	public void setF_SUMMARY_FINISHED(int count) {
//		setValue(F_SUMMARY_FINISHED,count);
//	}
//
//	@Override
//	public void setF_SUMMARY_PROCESSING(int count) {
//		setValue(F_SUMMARY_PROCESSING,count);
//	}

}
