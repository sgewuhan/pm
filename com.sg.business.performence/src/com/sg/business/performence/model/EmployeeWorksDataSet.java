package com.sg.business.performence.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;

public class EmployeeWorksDataSet {

	private List<EmployeeWorksNode> nodes = new ArrayList<EmployeeWorksNode>();

	public List<EmployeeWorksNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<EmployeeWorksNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * 传入userId 获得对应的节点，如果没有该用户id的节点返回null
	 * 
	 * @param userId
	 * @return
	 */
	public EmployeeWorksNode getNodeByUser(User user) {
		EmployeeWorksNode node = new EmployeeWorksNode(null, user);
		int index = nodes.indexOf(node);
		if (index == -1) {
			return null;
		} else {
			return nodes.get(index);
		}
	}

	public static EmployeeWorksDataSet getInstance(int year, int month,
			boolean onlyProjectWorks, boolean onlyOwnerDepartmentWorks,
			String userId) {
		// 获取工作集合
		Set<DBObject> workDataList = getWorks(year, month, userId,
				onlyProjectWorks, onlyOwnerDepartmentWorks);
		EmployeeWorksDataSet ds = new EmployeeWorksDataSet();
		Iterator<DBObject> iter = workDataList.iterator();
		while (iter.hasNext()) {
			// 遍历检索出的work数据，构造数据集模型
			DBObject data = iter.next();
			// 获得工作的负责人id
			String chargerId = (String) data.get(Work.F_CHARGER);
			User charger = UserToolkit.getUserById(chargerId);
			EmployeeWorksNode node = ds.getNodeByUser(charger);
			if (node == null) {
				// 没有对应用户的节点
				node = new EmployeeWorksNode(null, charger);
				// 并且添加到nodes中
				ds.nodes.add(node);
			}
			// 接下来需要将工作添加到EmplyeeNode
			Work work = ModelService.createModelObject(data, Work.class);
			node.addWork(work);
		}

		return ds;
	}

	private static Set<DBObject> getWorks(int year, int month, String userId,
			boolean onlyProjectWorks, boolean onlyOwnerDepartmentWorks) {
		Date[] between = getDateBetween(year, month);

		User user = UserToolkit.getUserById(userId);
		List<String> userIds = null;
		if (onlyOwnerDepartmentWorks || !onlyProjectWorks) {
			userIds = getAllUserInOrganization(user);
		}

		Set<DBObject> projectStage = getProjectStage(user, between);

		Set<DBObject> resultWorks = new HashSet<DBObject>();

		Iterator<DBObject> iter = projectStage.iterator();
		while (iter.hasNext()) {
			DBObject next = iter.next();
			Object projectId = next.get(Work.F_PROJECT_ID);
			Object stepName = next.get(Work.F_STATISTICS_STEP);
			Set<DBObject> result = getResultWork(projectId, stepName, between,
					onlyOwnerDepartmentWorks, userIds);
			if (result != null) {
				resultWorks.addAll(result);
			}
		}

		// 接下来需要处理独立工作
		// 首先获得本组织及下级组织的所有用户
		// 获得这些用户完成的独立工作
		if (!onlyProjectWorks) {
			List<DBObject> standloneWorks = getStandloneWorks(userIds, between);
			resultWorks.addAll(standloneWorks);
		}
		return resultWorks;
	}

	private static List<DBObject> getStandloneWorks(List<String> userIds,
			Date[] between) {
		BasicDBObject query = new BasicDBObject();
		query.put(Work.F_LIFECYCLE, Work.STATUS_FINIHED_VALUE);
		query.put(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE);
		query.put(Work.F_JOIN_PROJECT_CALCWORKS, Boolean.FALSE);
		query.put(Work.F_CHARGER, new BasicDBObject().append("$in", userIds));
		query.put(
				Work.F_ACTUAL_FINISH,
				new BasicDBObject().append("$gte", between[0]).append("$lt",
						between[1]));
		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		DBCursor cursor = workCol.find(query);
		List<DBObject> result = cursor.toArray();
		cursor.close();
		return result;
	}

	private static List<String> getAllUserInOrganization(User user) {
		ArrayList<String> userIds = new ArrayList<String>();
		Organization organization = user.getFunctionOrganization();
		List<ObjectId> subOrgIds = getSubOrganizationId(organization);
		DBCollection userCol = getCollection(IModelConstants.C_USER);
		DBCursor cursor = userCol.find(new BasicDBObject().append(
				User.F_ORGANIZATION_ID,
				new BasicDBObject().append("$in", subOrgIds)),
				new BasicDBObject().append(User.F_USER_ID, 1));
		while (cursor.hasNext()) {
			String _userId = (String) cursor.next().get(User.F_USER_ID);
			userIds.add(_userId);
		}
		cursor.close();
		return userIds;
	}

	private static Set<DBObject> getResultWork(Object projectId,
			Object stepName, Date[] between, boolean onlyOwnerDepartmentWorks,
			List<String> userIds) {
		Set<DBObject> result = new HashSet<DBObject>();

		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		// 判断该统计点，该项目的工作都已经完成
		long cnt = workCol.count(new BasicDBObject()
				.append(Work.F_STATISTICS_STEP, stepName)
				.append(Work.F_PROJECT_ID, projectId)
				.append(Work.F_LIFECYCLE,
						new BasicDBObject().append("$nin", new String[] {
								Work.STATUS_FINIHED_VALUE,
								Work.STATUS_CANCELED_VALUE })));
		if (cnt > 0) {// 存在没完成并且没取消的工作
			return null;
		}
		DBCursor workCursor = workCol.find(new BasicDBObject()
				.append(Work.F_STATISTICS_STEP, stepName)
				.append(Work.F_PROJECT_ID, projectId)
				.append(Work.F_LIFECYCLE, Work.STATUS_FINIHED_VALUE));
		workCursor.sort(new BasicDBObject().append(Work.F_ACTUAL_FINISH, -1));
		List<DBObject> temp = workCursor.toArray();
		workCursor.close();

		if (temp.isEmpty()) {
			return null;
		}
		// 判断第一条工作的完成时间
		DBObject fristWork = temp.get(0);
		Date af = (Date) fristWork.get(Work.F_ACTUAL_FINISH);
		if (af.before(between[0]) || af.after(between[1])) {
			return null;
		}
		
		if(!onlyOwnerDepartmentWorks){
			result.addAll(temp);
		}else{
			for (int i = 0; i < temp.size(); i++) {
				DBObject work = temp.get(i);
				Object chargerId = work.get(Work.F_CHARGER);
				if(userIds.contains(chargerId)){
					result.add(work);
				}
			}
		}
		return result;
	}

	private static DBCollection getCollection(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

	private static Set<DBObject> getProjectStage(User user, Date[] between) {
		ObjectId[] projectIds = getProjectIds(user);

		DBObject query = new BasicDBObject();
		query.put(Work.F_PROJECT_ID,
				new BasicDBObject().append("$in", projectIds));
		query.put(Work.F_STATISTICS_POINT, Boolean.TRUE);
		query.put(Work.F_ACTUAL_FINISH,
				new BasicDBObject().append("$lt", between[1]));

		DBObject fields = new BasicDBObject();
		fields.put(Work.F__ID, 1);
		fields.put(Work.F_STATISTICS_STEP, 1);
		fields.put(Work.F_PROJECT_ID, 1);

		DBCollection workCol = getCollection(IModelConstants.C_WORK);
		DBCursor cursor = workCol.find(query, fields);
		Set<DBObject> projectStage = new HashSet<DBObject>();
		// 排除重复项
		while (cursor.hasNext()) {
			DBObject next = cursor.next();
			Object projectId = next.get(Work.F_PROJECT_ID);
			Object stageName = next.get(Work.F_STATISTICS_STEP);
			if (stageName == null || projectId == null) {
				continue;
			}
			projectStage.add(new BasicDBObject().append(Work.F_PROJECT_ID,
					projectId).append(Work.F_STATISTICS_STEP, stageName));
		}
		return projectStage;
	}

	private static Date[] getDateBetween(int year, int month) {
		Calendar today = Calendar.getInstance();
		// 取出给定月的开始时间
		today.set(Calendar.YEAR, year);
		today.set(Calendar.MONTH, month);
		today.set(Calendar.DATE, 1);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		Date start = today.getTime();
		today.add(Calendar.MONTH, 1);
		Date end = today.getTime();
		return new Date[] { start, end };
	}

	private static ObjectId[] getProjectIds(User user) {
		List<PrimaryObject> orgList = user
				.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_WORKS_STATISTICS_ID);
		ObjectId[] orgIdList = new ObjectId[orgList.size()];
		for (int i = 0; i < orgList.size(); i++) {
			Organization org = (Organization) orgList.get(i);
			orgIdList[i] = org.get_id();
		}
		DBCollection projCol = getCollection(IModelConstants.C_PROJECT);
		DBCursor cursor = projCol.find(
				new BasicDBObject().append(Project.F_FUNCTION_ORGANIZATION,
						new BasicDBObject().append("$in", orgIdList)).append(
						Project.F_LIFECYCLE,
						new BasicDBObject().append("$nin", new String[] {
								Project.STATUS_NONE_VALUE,
								Project.STATUS_ONREADY_VALUE })),
				new BasicDBObject().append(Project.F__ID, 1));
		ObjectId[] projectIds = new ObjectId[cursor.count()];
		int i = 0;
		while (cursor.hasNext()) {
			projectIds[i++] = (ObjectId) cursor.next().get(Project.F__ID);
		}
		cursor.close();
		return projectIds;
	}

	private static List<ObjectId> getSubOrganizationId(Organization organization) {
		List<ObjectId> result = new ArrayList<ObjectId>();
		result.add(organization.get_id());
		List<PrimaryObject> children = organization.getChildrenOrganization();
		if (children != null && children.size() > 0) {
			for (PrimaryObject child : children) {
				result.addAll(getSubOrganizationId((Organization) child));
			}
		}
		return result;
	}

}
