package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class DelayWork extends SingleDBCollectionDataSetFactory {
	private User user;
	private DBCollection userCol;
	private DBCollection projectCol;

	public DelayWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
		userCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_USER);
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
		DBCollection collection = getCollection();
		DBObject condition = getCondition();
		DBCursor cur = collection.find(condition);
		while (cur.hasNext()) {
			DBObject next = cur.next();
			Work work = ModelService.createModelObject(next, Work.class);
			if (isDelayWork(work)) {
				dataItems.add(work);
			}
		}
		return new DataSet(dataItems);
	}

	// 判断工作是否延期
	private boolean isDelayWork(Work work) {
		List<PrimaryObject> childrenWork = work.getChildrenWork();
		if (childrenWork != null && !childrenWork.isEmpty()) {
			return false;
		} else {
			if (ILifecycle.STATUS_FINIHED_VALUE.equals(work
					.getLifecycleStatus())) {
				return work.isDelayFinish();
			} else {
				return work.isDelayFinish();
			}
		}

	}

	// 返回查询条件
	private DBObject getCondition() {

		int year = -1;
		int month = -1;
		Date start = null;
		Date stop = null;
		Calendar calendar = Calendar.getInstance();
		DBObject date = getQueryCondition();
		if (date != null) {
			year = (int) date.get("year"); //$NON-NLS-1$
			month = (int) date.get("month"); //$NON-NLS-1$

			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			stop = calendar.getTime();
			setQueryCondition(null);

		} else {
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

		// 状态为已完成或者进行中
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));

		BasicDBObject ucondition = new BasicDBObject();
		ucondition.put(
				"$or", //$NON-NLS-1$
				new BasicDBObject[] {
						// 工作负责人为当前用户所管理部门下的职员
						new BasicDBObject().append(Work.F_CHARGER,
								new BasicDBObject().append("$in", //$NON-NLS-1$
										getUserIdSet())),
						// 项目id为当前用户所管理的项目id
						new BasicDBObject().append(Work.F_PROJECT_ID,
								new BasicDBObject().append("$in", //$NON-NLS-1$
										getUsersFunctionProjectIds())),
						// 项目id为当前用户所负责的项目id
						new BasicDBObject().append(Work.F_PROJECT_ID,
								new BasicDBObject().append("$in", //$NON-NLS-1$
										getUsersChargerProjectIds())),

						// 工作负责人或参与者为当前用户
						new BasicDBObject().append(Work.F_PARTICIPATE,
								user.getUserid())

				});

		BasicDBObject dcondition = new BasicDBObject();
		dcondition.put(
				"$or", //$NON-NLS-1$
				new BasicDBObject[] {

						new BasicDBObject().append(Work.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start) //$NON-NLS-1$
										.append("$lte", stop)), //$NON-NLS-1$

						new BasicDBObject().append(Work.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start) //$NON-NLS-1$
										.append("$lte", stop)), //$NON-NLS-1$

						new BasicDBObject().append(Work.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start) //$NON-NLS-1$
										.append("$lte", stop)), //$NON-NLS-1$

						new BasicDBObject().append(
								"$and", //$NON-NLS-1$
								new BasicDBObject[] {
										new BasicDBObject().append(
												Work.F_ACTUAL_START,
												new BasicDBObject().append(
														"$lte", start)), //$NON-NLS-1$
										new BasicDBObject().append(
												Work.F_ACTUAL_FINISH,
												new BasicDBObject().append(
														"$gte", stop)) }) }); //$NON-NLS-1$

		dbo.put("$and", new BasicDBObject[] { ucondition, dcondition }); //$NON-NLS-1$
		return dbo;
	}

	// 返回
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<ObjectId> getUserIdSet() {
		Object ids = getOrganizationIdCascade(null).toArray();
		List distinct = userCol.distinct(User.F_USER_ID, new BasicDBObject()
				.append(User.F_ORGANIZATION_ID,
						new BasicDBObject().append("$in", ids))); //$NON-NLS-1$
		return distinct;
	}

	// 返回用户具有管理者角色的部门及下级部门的ID
	private Collection<? extends ObjectId> getOrganizationIdCascade(
			Organization org) {
		Set<ObjectId> result = new HashSet<ObjectId>();
		List<PrimaryObject> orglist;
		if (org != null) {
			result.add(org.get_id());
			orglist = org.getChildrenOrganization();
		} else {
			orglist = getUsersManagedOrganization();
		}
		for (int i = 0; i < orglist.size(); i++) {
			result.addAll(getOrganizationIdCascade((Organization) orglist
					.get(i)));
		}
		return result;
	}

	// 返回获取用户具有管理者角色的部门及下级部门
	private List<PrimaryObject> getUsersManagedOrganization() {
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

	// 返回项目管理员管理的项目ID
	private List<ObjectId> getUsersFunctionProjectIds() {
		List<PrimaryObject> orglist = user
				.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_PROJECT_ADMIN_ID);
		ObjectId[] ids = new ObjectId[orglist.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = orglist.get(i).get_id();
		}
		List<ObjectId> list = new ArrayList<ObjectId>();
		DBCursor pids = projectCol.find(new BasicDBObject().append(
				Project.F_FUNCTION_ORGANIZATION,
				new BasicDBObject().append("$in", ids)), new BasicDBObject() //$NON-NLS-1$
				.append(Project.F__ID, 1));
		while (pids.hasNext()) {
			DBObject next = pids.next();
			list.add((ObjectId) next.get(Project.F__ID));
		}
		return list;
	}

	// 项目负责人
	List<ObjectId> getUsersChargerProjectIds() {
		List<ObjectId> list = new ArrayList<ObjectId>();
		DBCursor pids = projectCol
				.find(new BasicDBObject().append(Project.F_CHARGER,
						user.getUserid()),
						new BasicDBObject().append(Project.F__ID, 1));
		while (pids.hasNext()) {
			DBObject next = pids.next();
			list.add((ObjectId) next.get(Project.F__ID));
		}
		return list;
	}

}
