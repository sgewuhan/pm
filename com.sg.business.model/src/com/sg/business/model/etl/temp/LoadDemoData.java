package com.sg.business.model.etl.temp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.Work;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class LoadDemoData implements ISchedualJobRunnable {

	private DBCollection userCol;
	private DBCollection orgCol;
	private DBCollection pjCol;
	private DBCollection worksCol;

	private void init() {
		userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);

		orgCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		pjCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);

		worksCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORKS_PERFORMENCE);
	}

	@Override
	public boolean run() throws Exception {
		// 检索空气弹簧项目部的所有完成的项目，项目负责人 每天在每个项目上添加工时记录4
		// 项目参与者在每个项目上添加工时记录1小时
		init();
		ArrayList<DBObject> result = new ArrayList<DBObject>();

		DBCursor cursor = orgCol.find(null,
				new BasicDBObject().append("desc", 1));
		while (cursor.hasNext()) {
			DBObject orgdata = cursor.next();
			System.out.println("处理组织：" + orgdata);
			result.addAll(loadOrgProjectData((ObjectId) orgdata.get("_id")));
			System.out.println("\n");
		}

		if (!result.isEmpty()) {
			System.out.println("准备插入数据.");
			worksCol.insert(result);
			System.out.println("插入工时数据完成.");
		}
		return true;
	}

	private Collection<? extends DBObject> loadOrgProjectData(ObjectId orgId) {

		ArrayList<DBObject> result = new ArrayList<DBObject>();

		// 获得承担部门为orgId的项目

		DBCursor cur = pjCol.find(new BasicDBObject().append(
				Project.F_LAUNCH_ORGANIZATION, orgId).append(
				Project.F_LIFECYCLE, Project.STATUS_FINIHED_VALUE));
		while (cur.hasNext()) {
			DBObject pjData = cur.next();
			System.out.println("--处理项目:" + pjData.get("desc"));
			result.addAll(loadProjectData(pjData));
		}
		return result;
	}

	private List<DBObject> loadProjectData(DBObject pjData) {
		// 获得实际开始时间和实际完成时间
		ArrayList<DBObject> result = new ArrayList<DBObject>();
		System.out.print("----");
		Date as = (Date) pjData.get(Project.F_ACTUAL_START);
		Date af = (Date) pjData.get(Project.F_ACTUAL_FINISH);
		// 获得负责人 和参与者
		String chargerId = (String) pjData.get(Project.F_CHARGER);
		List participate = (List) pjData.get(Project.F_PARTICIPATE);

		HashSet<String> users = new HashSet<String>();
		if (chargerId != null) {
			users.add(chargerId);
		}
		if (participate != null && participate.size() > 0) {
			users.addAll(participate);
		}

		// 获得项目根工作Id
		ObjectId workId = (ObjectId) pjData.get(Project.F_WORK_ID);
		if (workId == null) {
			Project project = ModelService.createModelObject(pjData,
					Project.class);
			BasicDBObject wbsRootData = new BasicDBObject();
			wbsRootData.put(Work.F_DESC, project.getDesc());
			wbsRootData.put(Work.F_LIFECYCLE, Work.STATUS_ONREADY_VALUE);
			wbsRootData.put(Work.F_PROJECT_ID, project.get_id());
			workId = new ObjectId();
			wbsRootData.put(Work.F__ID, workId);
			wbsRootData.put(Work.F_ROOT_ID, workId);
			wbsRootData.put(Work.F_IS_PROJECT_WBSROOT, Boolean.TRUE);
			worksCol.insert(wbsRootData);
			pjCol.update(new BasicDBObject().append("_id", project.get_id()),
					new BasicDBObject().append("$set", new BasicDBObject()
							.append(Project.F_WORK_ID, workId)));
		}
		if (as == null || af == null || users.isEmpty()) {
			return result;
		}
		System.out.print(String.format(Utils.FORMATE_DATE_SIMPLE, as));
		System.out.print(" " + String.format(Utils.FORMATE_DATE_SIMPLE, af));
		System.out.println();

		long startDateCode = as.getTime() / (1000 * 60 * 60 * 24);
		long endDateCode = af.getTime() / (1000 * 60 * 60 * 24);

		Iterator<String> iter = users.iterator();
		while (iter.hasNext()) {
			String userid = iter.next();
			long cnt = userCol.count(new BasicDBObject().append(User.F_USER_ID,
					userid));
			if (cnt == 0) {
				continue;
			}

			ArrayList<DBObject> userDatas = createUserData(userid,
					startDateCode, endDateCode, workId, pjData);
			result.addAll(userDatas);
		}
		return result;

	}

	private ArrayList<DBObject> createUserData(String userid,
			long startDateCode, long endDateCode, ObjectId workId,
			DBObject pjData) {
		ArrayList<DBObject> result = new ArrayList<DBObject>();

		for (long i = startDateCode; i <= endDateCode; i++) {
			BasicDBObject userdata = new BasicDBObject();
			userdata.put("workid", workId);
			userdata.put("userid", userid);
			userdata.put("commitdate", new Date());
			userdata.put("datecode", i);
			userdata.put("projectdesc", pjData.get("desc"));
			userdata.put("project_id", pjData.get("_id"));
			userdata.put("workdesc", pjData.get("desc"));
			userdata.put("_editor", "editor.create.workrecord");
			userdata.put("desc", "正常进行");
			userdata.put("content", "演示生成");
			userdata.put("works", 1d);
			result.add(userdata);
			System.out.println("------添加工时数据:" + userdata);
		}
		return result;
	}

}
