package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;

public class ProjectTypeProjectProvider extends ProjectProvider {
	private DBCollection projectCol;
	private String userId;
	public ProjectTypeProjectProvider(String desc,String userId) {
		super();
		
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		set_data(new BasicDBObject());
		setValue(F_DESC, desc);
		this.userId=userId;
		
	}

	@Override
	public List<PrimaryObject> getProjectSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		List<PrimaryObject> projectSet = getProjectSet(result);
		return projectSet;
	}

	private List<PrimaryObject> getProjectSet(List<PrimaryObject> result) {

		DBCursor cur = projectCol
				.find(getQueryCondtion());

		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			Project project = ModelService
					.createModelObject(dbo, Project.class);

			result.add(project);
		}


		return result;
	}
	
	
	private DBObject getQueryCondtion() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date start = calendar.getTime();
		calendar.add(Calendar.YEAR, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		Date stop = calendar.getTime();

		DBObject dbo = new BasicDBObject();
		dbo.put(Project.F_PROJECT_TYPE_OPTION,getDesc());
		dbo.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", getUerOrgId()));
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
												.append("&lte", stop)),
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
	
	
	protected List<ObjectId> getUerOrgId() {
		List<ObjectId> list = new ArrayList<ObjectId>();
		List<PrimaryObject> userOrg = getUserOrg(
				new ArrayList<PrimaryObject>(), getInput());
		for (PrimaryObject po : userOrg) {
			list.add(po.get_id());
		}
		return list;

	}

	protected List<PrimaryObject> getUserOrg(List<PrimaryObject> list,
			List<PrimaryObject> childrenList) {
		list.addAll(childrenList);
		for (PrimaryObject po : childrenList) {
			List<PrimaryObject> childrenOrg = ((Organization) po)
					.getChildrenOrganization();
			getUserOrg(list, childrenOrg);
		}
		return list;
	}

	protected List<PrimaryObject> getInput() {
		// 获取当前用户担任管理者角色的部门
		User user=UserToolkit.getUserById(getUserId());		
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			boolean hasParent = false;
			for (int j = 0; j < input.size(); j++) {
				Organization inputOrg = (Organization) input.get(j);
				if (inputOrg.isSuperOf(org)) {
					hasParent = true;
					break;
				}
				if (org.isSuperOf(inputOrg)) {
					input.remove(j);
					break;
				}
			}
			if (!hasParent) {
				input.add(org);
			}
		}

		return input;
	}
	
	
	

	@Override
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		String imageUrl = "<img src='" + FileUtil.getImageURL(BusinessResource.IMAGE_BUSINESSUNIT_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER)
				+ "' style='float:left;padding:2px' width='24' height='24' />";
		String label = getDesc();
		sb.append(imageUrl);
		sb.append("<b>");
		sb.append(label);
		sb.append("</b>");
		sb.append("<br/>");
		sb.append("</span>");
		return sb.toString();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getProjectSetName() {
		// TODO Auto-generated method stub
		return null;
	}


}
