package com.sg.business.visualization.labelprovide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTypeProjectProvider;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

public class ProjectTypeProjectCount extends ColumnLabelProvider {

	private DBCollection projectCol;
	private User user;

	public ProjectTypeProjectCount() {
		super();
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public String getText(Object element) {
		PrimaryObject dbo = ((PrimaryObject) element);
		if (dbo instanceof ProjectTypeProjectProvider) {
			ProjectTypeProjectProvider projectTypeProjectProvider = (ProjectTypeProjectProvider) dbo;
			long cnt = getCountOfYear(projectTypeProjectProvider);
			long wipCnt = getWipCount(projectTypeProjectProvider);
			StringBuffer sb = new StringBuffer();
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;font-weight:bold;color:#99cc00'>");
			sb.append(wipCnt);
			sb.append("</span>");
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt;font-weight:bold'>");
			sb.append("/" + cnt);
			sb.append("</span>");

			sb.append("<a href=\""
					+ projectTypeProjectProvider.getDesc()+","+projectTypeProjectProvider.getUserId()
					+ "\" target=\"_rwt\">");
			sb.append("<img src='");
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_GO_24,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='24' height='24' />");
			sb.append("</a>");
			return sb.toString();
		}
		return "";
	}

	private long getCountOfYear(
			ProjectTypeProjectProvider projectTypeProjectProvider) {
		long count = projectCol
				.count(getQueryCondtion(projectTypeProjectProvider));
		return count;
	}

	private long getWipCount(
			ProjectTypeProjectProvider projectTypeProjectProvider) {

		long count = projectCol.count(new BasicDBObject()
				.append(Project.F_PROJECT_TYPE_OPTION,
						projectTypeProjectProvider.getDesc())
				.append(ILifecycle.F_LIFECYCLE, ILifecycle.STATUS_WIP_VALUE)
				.append(Project.F_LAUNCH_ORGANIZATION,
						new BasicDBObject().append("$in", getUerOrgId())));
		return count;

	}

	private DBObject getQueryCondtion(
			ProjectTypeProjectProvider projectTypeProjectProvider) {
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
		dbo.put(Project.F_PROJECT_TYPE_OPTION,
				projectTypeProjectProvider.getDesc());
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

}
