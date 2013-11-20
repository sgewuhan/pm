package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.resource.BusinessResource;

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
		List<PrimaryObject> projectSet = getProjectSet(organization, result);

		//TODO 完成查询后，需要写入合计值
		//参考ProjectProvider的F开头的字段的注释，将值写入
		//使用ProjectProvider.setSummaryDate(data)方法
		//data是合计值的Map<String,Object> Map的key 为ProjectProvider的F开头的字段
		return projectSet;
	}

	private List<PrimaryObject> getProjectSet(PrimaryObject po,
			List<PrimaryObject> result) {
		//TODO 获取传入的条件，修改现有代码使之能根据条件进行查询
		//使用getStartDate()获得开始时间，使用getFinishDate()获得完成时间
		//错误抛出方法外
		

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

	@Override
	public String getProjectSetName() {
		return getDesc()+"项目集";
	}

}
