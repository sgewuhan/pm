package com.sg.business.model.etl.eai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.RNDPeriodCost;
import com.sg.business.model.WorkOrderPeriodCost;
import com.sg.business.model.nls.Messages;

/**
 * ʹ����Ŀʵ�ʿ�ʼʱ������ʱ������з��ɱ���̯ �������ڳ����ݵĵ���
 * 
 * @author Administrator
 * 
 */
public class WorkorderPeriodCostAllocate2 {

	public static final String COSECENTERCODE = "cost"; //$NON-NLS-1$
	public static final String ACCOUNTNUMERS = "account"; //$NON-NLS-1$
	public static final String YEAR = "year"; //$NON-NLS-1$
	public static final String MONTH = "month"; //$NON-NLS-1$
	public static final String RNDCOST = "rndcost"; //$NON-NLS-1$
	private DBCollection costAllocateCol;
	private DBCollection projectCol;

	public WorkorderPeriodCostAllocate2() {
		costAllocateCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	public Collection<? extends WorkOrderPeriodCost> getData(
			Map<String, Object> parameter) {

		Object year = parameter.get(YEAR);
		Object month = parameter.get(MONTH);
		if (!(year instanceof Integer) || !(month instanceof Integer)) {
			throw new IllegalArgumentException(Messages.get().WorkorderPeriodCostAllocate2_5);
		}

		Object costCenterCode = parameter.get(COSECENTERCODE);
		if (!(costCenterCode instanceof String)) {
			throw new IllegalArgumentException(Messages.get().WorkorderPeriodCostAllocate2_6);
		}

		Object account = parameter.get(ACCOUNTNUMERS);
		if (account != null && !(account instanceof String[])) {
			throw new IllegalArgumentException(Messages.get().WorkorderPeriodCostAllocate2_7);
		}

		Object rndCost = parameter.get(RNDCOST);
		if (!(rndCost instanceof RNDPeriodCost)) {
			throw new IllegalArgumentException(Messages.get().WorkorderPeriodCostAllocate2_8);
		}

		// 1. ���ݳɱ����Ļ����֯
		RNDPeriodCost rndpc = ((RNDPeriodCost) rndCost);
		Organization org = rndpc.getOrganization();

		if (org == null) {
			throw new IllegalArgumentException(Messages.get().WorkorderPeriodCostAllocate2_9);
		}

		Organization company = org.getCompany();
		if (company == null) {
			throw new IllegalArgumentException(Messages.get().WorkorderPeriodCostAllocate2_10);
		}

		// �����֯�¼��������ڽ��е���Ŀ
		List<Organization> list = company.getOrganizationStructure();
		ObjectId[] orgids = new ObjectId[list.size()];
		for (int i = 0; i < orgids.length; i++) {
			orgids[i] = list.get(i).get_id();
		}

		// �е���֯��orgids��,�ڵ��´��ڽ���״̬�ĵ���Ŀ�Ĺ������
		Calendar cal = Calendar.getInstance();
		cal.set(((Integer) year).intValue(),
				((Integer) month).intValue() - 1, 1, 0, 0, 0);
		Date stop = cal.getTime();

		// ����ȡ��һ�·ݵ�һ�������
		cal.add(Calendar.MONTH, 1);
		Date start = cal.getTime();

		BasicDBObject query = new BasicDBObject();
		query.put(Project.F_LAUNCH_ORGANIZATION,
				new BasicDBObject().append("$in", orgids)); //$NON-NLS-1$
		query.put(
				"$and", //$NON-NLS-1$
				new BasicDBObject[] {
						new BasicDBObject().append(
								"$or", //$NON-NLS-1$
								new BasicDBObject[] {
										// ���ʱ��Ϊ��
										// �������ʱ�����ǰһ�µ����һ��
										new BasicDBObject().append(
												Project.F_ACTUAL_FINISH, null),
										new BasicDBObject().append(
												Project.F_ACTUAL_FINISH,
												new BasicDBObject().append(
														"$gte", stop)) }), //$NON-NLS-1$
														
						new BasicDBObject().append(
								"$or", //$NON-NLS-1$
								new BasicDBObject[] {
										//��ʼʱ����벻Ϊ��
										//��ʼʱ�����С�ڵ��µ����һ��
										new BasicDBObject().append(
												Project.F_ACTUAL_START,
												new BasicDBObject().append(
														"$ne", null)), //$NON-NLS-1$
										new BasicDBObject().append(
												Project.F_ACTUAL_START,
												new BasicDBObject().append(
														"$lt", start)) }) }); //$NON-NLS-1$

		@SuppressWarnings("rawtypes")
		List workorders = projectCol.distinct(Project.F_WORK_ORDER, query);

		// Ӧ�����������ɱ����ĵĹ������
		Set<String> effectiveWorkOrders = new HashSet<String>();

		// ������ֶ���������ŵģ�������֯������ŵĶ�Ӧ�Ƿ���ȷ������Ǹóɱ����ĵĹ�����ţ�����ʱ���ݱ������ù������
		for (int i = 0; i < workorders.size(); i++) {
			String workOrder = (String) workorders.get(i);
			if (company.hasWorkOrder(workOrder)) {
				effectiveWorkOrders.add(workOrder);
			}
		}

		// ���з��ɱ�ƽ̯��ÿ���������
		if (effectiveWorkOrders.isEmpty()) {
			Commons.logerror(Messages.get().WorkorderPeriodCostAllocate2_18 + costCenterCode + company + Messages.get().WorkorderPeriodCostAllocate2_19
					+ year + month + Messages.get().WorkorderPeriodCostAllocate2_20);
			return new ArrayList<WorkOrderPeriodCost>();
		}

		List<DBObject> toBeInsert = new ArrayList<DBObject>();
		Iterator<?> iter = effectiveWorkOrders.iterator();
		while (iter.hasNext()) {
			String workOrderNumber = (String) iter.next();
			DBObject wopc = new BasicDBObject();
			wopc.put(WorkOrderPeriodCost.F_WORKORDER, workOrderNumber);
			wopc.put(WorkOrderPeriodCost.F_YEAR, year);
			wopc.put(WorkOrderPeriodCost.F_MONTH, month);
			wopc.put(WorkOrderPeriodCost.F_COSTCENTERCODE, costCenterCode);

			Iterator<String> iter2 = rndpc.get_data().keySet().iterator();
			while (iter2.hasNext()) {
				String key = iter2.next();
				if (!Utils.isNumbers(key)) {// �����������͵��ֶκ���
					continue;
				}
				Object cost = rndpc.getValue(key);
				if (cost == null) {
					wopc.put(key, 0d);
				} else {
					double value = ((Double) cost) / effectiveWorkOrders.size();
					wopc.put(key, value);
				}
			}

			toBeInsert.add(wopc);
		}

		if (toBeInsert.size() > 0) {
			costAllocateCol.insert(toBeInsert);
		}

		// ׼������
		ArrayList<WorkOrderPeriodCost> result = new ArrayList<WorkOrderPeriodCost>();
		for (int i = 0; i < toBeInsert.size(); i++) {
			result.add(ModelService.createModelObject(toBeInsert.get(i),
					WorkOrderPeriodCost.class));
		}

		return result;
	}

	public Date[] getStartAndEnd(Integer year, Integer month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date start = cal.getTime();
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MILLISECOND, -1);
		Date end = cal.getTime();
		return new Date[] { start, end };
	}

}
