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
import com.sg.business.model.ILifecycle;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class DelayWork extends SingleDBCollectionDataSetFactory {
	private User user;

	public DelayWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
		DBCollection collection = getCollection();
		DBCursor cur = collection.find(getCondition());
		while (cur.hasNext()) {
			DBObject next = cur.next();
			Work work = ModelService.createModelObject(next, Work.class);
			if (isDelayWork(work)) {
				dataItems.add(work);
			}
		}
		return new DataSet(dataItems);
	}

	private boolean isDelayWork(Work work) {
		List<PrimaryObject> childrenWork = work.getChildrenWork();
		if (childrenWork != null && !childrenWork.isEmpty()) {
			return false;
		} else {
			if (ILifecycle.STATUS_FINIHED_VALUE.equals(work
					.getLifecycleStatus())) {
				return work.isDelayed();
			} else {
				return work.isDelayNow();
			}
		}

	}

	private DBObject getCondition() {

		int year=-1;
		int month=-1;
        Date start=null;
        Date stop=null;
        Calendar calendar = Calendar.getInstance();
		DBObject date = getQueryCondition();
		if(date!=null){
			year = (int) date.get("year");
			month = (int) date.get("month");
			
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
			
		}else{
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
		dbo.put(Work.F_CHARGER,
				new BasicDBObject().append("$in", getUserIdSet()));
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] {
						ILifecycle.STATUS_FINIHED_VALUE,
						ILifecycle.STATUS_WIP_VALUE }));
		dbo.put("$or",
				new BasicDBObject[] {

						new BasicDBObject().append(Work.F_ACTUAL_START,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Work.F_PLAN_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(Work.F_ACTUAL_FINISH,
								new BasicDBObject().append("$gte", start)
										.append("$lte", stop)),

						new BasicDBObject().append(
								"$and",
								new BasicDBObject[] {
										new BasicDBObject().append(
												Work.F_ACTUAL_START,
												new BasicDBObject().append(
														"$lte", start)),
										new BasicDBObject().append(
												Work.F_ACTUAL_FINISH,
												new BasicDBObject().append(
														"$gte", stop)) }) });
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
		// ��ȡ��ǰ�û����ι����߽�ɫ�Ĳ���
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