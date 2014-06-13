package com.sg.business.performence.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	 * ����userId ��ö�Ӧ�Ľڵ㣬���û�и��û�id�Ľڵ㷵��null
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
			String userId) {
		// ��ȡ��������
		List<DBObject> workDataList = getWorks(year, month, userId);
		EmployeeWorksDataSet ds = new EmployeeWorksDataSet();

		// ������������work���ݣ��������ݼ�ģ��
		for (int i = 0; i < workDataList.size(); i++) {
			DBObject data = workDataList.get(i);
			// ��ù����ĸ�����id
			ObjectId chargerId = (ObjectId) data.get(Work.F_CHARGER);
			User charger = ModelService
					.createModelObject(User.class, chargerId);
			EmployeeWorksNode node = ds.getNodeByUser(charger);
			if (node == null) {
				// û�ж�Ӧ�û��Ľڵ�
				node = new EmployeeWorksNode(null, charger);
				// ������ӵ�nodes��
				ds.nodes.add(node);
			}
			// ��������Ҫ��������ӵ�EmplyeeNode
			Work work = ModelService.createModelObject(data, Work.class);
			node.addWork(work);
		}

		return ds;
	}

	private static List<DBObject> getWorks(int year, int month, String userId) {
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> orgList = user
				.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_WORKS_STATISTICS_ID);
		ObjectId[] orgIdList = new ObjectId[orgList.size()];
		for (int i = 0; i < orgList.size(); i++) {
			Organization org = (Organization) orgList.get(i);
			orgIdList[i] = org.get_id();
		}
		DBCollection projCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		DBCursor cursor = projCol.find(new BasicDBObject().append(
				Project.F_FUNCTION_ORGANIZATION,
				new BasicDBObject().append("$in", orgIdList)),
				new BasicDBObject().append(Project.F__ID, 1));
		ObjectId[] projectIds = new ObjectId[cursor.count()];
		int i = 0;
		while (cursor.hasNext()) {
			projectIds[i++] = (ObjectId) cursor.next().get(Project.F__ID);
		}
		cursor.close();
		DBCollection workCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);

		// int year = 2014;
		// int month = 5;// 0����1��
		Calendar today = Calendar.getInstance();
		// ȡ�������µĿ�ʼʱ��
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

		DBObject query = new BasicDBObject();
		query.put(Work.F_PROJECT_ID,
				new BasicDBObject().append("$in", projectIds));
		query.put(Work.F_STATISTICS_POINT, Boolean.TRUE);
		query.put("$gte",
				new BasicDBObject().append(Work.F_ACTUAL_FINISH, start));
		query.put("$lt", new BasicDBObject().append(Work.F_ACTUAL_FINISH, end));

		DBObject fields = new BasicDBObject();
		fields.put(Work.F__ID, 1);
		fields.put(Work.F_STATISTICS_STEP, 1);
		fields.put(Work.F_PROJECT_ID, 1);

		cursor = workCol.find(query, fields);
		List<DBObject> resultWorks = new ArrayList<DBObject>();
		while (cursor.hasNext()) {
			DBObject workData = (DBObject) cursor.next();
			Object stepName = workData.get(Work.F_STATISTICS_STEP);
			Object projectId = workData.get(Work.F_PROJECT_ID);
			// �жϸ�ͳ�Ƶ㣬����Ŀ�Ĺ������Ѿ����
			long cnt = workCol.count(new BasicDBObject()
					.append(Work.F_STATISTICS_STEP, stepName)
					.append(Work.F_PROJECT_ID, projectId)
					.append(Work.F_LIFECYCLE,
							new BasicDBObject().append("$nin", new String[] {
									Work.STATUS_FINIHED_VALUE,
									Work.STATUS_CANCELED_VALUE })));
			if (cnt > 0) {// ����û��ɲ���ûȡ���Ĺ���
				continue;
			}
			DBCursor cursor1 = workCol
					.find(new BasicDBObject()
							.append(Work.F_STATISTICS_STEP, stepName)
							.append(Work.F_PROJECT_ID, projectId)
							.append(Work.F_LIFECYCLE, Work.STATUS_FINIHED_VALUE),
							new BasicDBObject().append(Work.F__ID, 1));
			resultWorks.addAll(cursor1.toArray());
			cursor1.close();
		}

		// ��������Ҫ�����������
		Organization organization = user.getFunctionOrganization();
		List<PrimaryObject> userList = organization.getUser();
		ObjectId[] userIdList = new ObjectId[userList.size()];
		for (int j = 0; j < userList.size(); j++) {
			User charger = (User) userList.get(j);
			userIdList[j] = charger.get_id();
		}
		DBCollection userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		DBCursor standloneCursor = userCol.find(new BasicDBObject()
				.append(Work.F_LIFECYCLE, Work.STATUS_FINIHED_VALUE)
				.append(Work.F_WORK_TYPE, Work.WORK_TYPE_STANDLONE)
				.append(Work.F_JOIN_PROJECT_CALCWORKS, Boolean.FALSE)
				.append(Work.F_CHARGER,
						new BasicDBObject().append("$in", userIdList)));
		resultWorks.addAll(standloneCursor.toArray());
		standloneCursor.close();

		return resultWorks;
	}

}
