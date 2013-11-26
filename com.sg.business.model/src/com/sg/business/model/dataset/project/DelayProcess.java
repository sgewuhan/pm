package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.UserTask;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class DelayProcess extends SingleDBCollectionDataSetFactory {
	private User user;

	public DelayProcess() {
		super(IModelConstants.DB, IModelConstants.C_USERTASK);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
		DBCollection collection = getCollection();
		DBCursor cur = collection.find(getCondition(null, null));
		while (cur.hasNext()) {
			DBObject next = cur.next();
			UserTask usertask = ModelService.createModelObject(next,
					UserTask.class);
			if (isDelayTask(usertask)) {
				dataItems.add(usertask);
			}
		}

		return new DataSet(dataItems);
	}

	private boolean isDelayTask(UserTask usertask) {
		Object value = usertask.getValue(UserTask.F_WORKITEMID);
		DBCollection collection = getCollection();

		DBObject inProgress = collection.findOne(new BasicDBObject().append(
				UserTask.F_WORKITEMID, value).append(UserTask.F_STATUS,
				"InProgress"));

		if (inProgress != null) {
			UserTask taskInProgress = ModelService.createModelObject(
					inProgress, UserTask.class);
			if ((taskInProgress.get_cdate().getTime() - usertask.get_cdate()
					.getTime()) / (1000 * 60 * 60) > 3) {
				return true;
			}
		} else {
			if (new Date().getTime() - usertask.get_cdate().getTime()
					/ (1000 * 60 * 60) > 4) {
				return true;
			}
		}
		return false;
	}

	private DBObject getCondition(Date start, Date stop) {

		if (start == null || stop == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			stop = calendar.getTime();
		}

		DBObject dbo = new BasicDBObject();
		dbo.put(UserTask.F_ACTUALOWNER,
				new BasicDBObject().append("$in", getUserIdSet()));
		dbo.put(UserTask.F_STATUS, "Reserved");

		dbo.put(UserTask.F__CDATE, new BasicDBObject().append("$gte", start)
				.append("$lte", stop));
		return dbo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<ObjectId> getUserIdSet() {
		DBCollection projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		List distinct = projectCol.distinct(User.F_USER_ID,
				new BasicDBObject()
						.append(User.F_ORGANIZATION_ID, new BasicDBObject()
								.append("$in", getOrganizationsId())));
		return distinct;
	}

	protected List<ObjectId> getOrganizationsId() {
		List<ObjectId> list = new ArrayList<ObjectId>();
		List<PrimaryObject> userOrg = getOrganizations(
				new ArrayList<PrimaryObject>(), getInput());
		for (PrimaryObject po : userOrg) {
			list.add(po.get_id());
		}
		return list;

	}

	protected List<PrimaryObject> getOrganizations(List<PrimaryObject> list,
			List<PrimaryObject> childrenList) {
		list.addAll(childrenList);
		for (PrimaryObject po : childrenList) {
			List<PrimaryObject> childrenOrg = ((Organization) po)
					.getChildrenOrganization();
			getOrganizations(list, childrenOrg);
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
