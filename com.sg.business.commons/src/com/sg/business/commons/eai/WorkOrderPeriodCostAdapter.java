package com.sg.business.commons.eai;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mobnut.commons.util.Utils;
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
import com.sg.business.model.RNDPeriodCost;
import com.sg.business.model.WorkOrderPeriodCost;
import com.sg.business.model.WorksPerformence;

public class WorkOrderPeriodCostAdapter {

	public static final String COSECENTERCODE = "cost";
	public static final String ACCOUNTNUMERS = "account";
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String RNDCOST = "rndcost";
	private DBCollection costAllocateCol;
	private DBCollection workPerformenceCol;
	private DBCollection projectCol;

	public WorkOrderPeriodCostAdapter() {
		costAllocateCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		workPerformenceCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORKS_PERFORMENCE);
		projectCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
	}

	public Collection<? extends WorkOrderPeriodCost> getData(
			Map<String, Object> parameter) {

		Object year = parameter.get(YEAR);
		Object month = parameter.get(MONTH);
		if (!(year instanceof Integer) || !(month instanceof Integer)) {
			throw new IllegalArgumentException("�ڼ� year, month��������");
		}

		Object costCenterCode = parameter.get(COSECENTERCODE);
		if (!(costCenterCode instanceof String)) {
			throw new IllegalArgumentException("�ɱ����Ĵ��� costcode ��������");
		}

		Object account = parameter.get(ACCOUNTNUMERS);
		if (account != null && !(account instanceof String[])) {
			throw new IllegalArgumentException("��Ŀ��  account ��������");
		}

		Object rndCost = parameter.get(RNDCOST);
		if (!(rndCost instanceof RNDPeriodCost)) {
			throw new IllegalArgumentException("�ɱ������з��ɱ�  rndcost ��������");
		}

		// 1. ���ݳɱ����Ļ����֯
		RNDPeriodCost rndpc = ((RNDPeriodCost) rndCost);
		Organization org = rndpc.getOrganization();

		if (org == null) {
			throw new IllegalArgumentException("�ɱ������޷���ö�Ӧ����֯");
		}

		// ׼�����湤����Ŷ�Ӧ��ʱ������
		Map<String, Double> workOrderMapWorks = new HashMap<String, Double>();

		Organization company = org.getCompany();

		// 2. ���������֯�·ǳɱ�������֯������Ա��userid
		Set<String> userIds = getCostCenterUserIdList(org);
		String[] userIdArr = userIds.toArray(new String[0]);

		// 3. �����Ա���ڼ�Ĺ�ʱ��¼
		BasicDBObject query = new BasicDBObject();
		query.put(WorksPerformence.F_USERID,
				new BasicDBObject().append("$in", userIdArr));

		Date[] period = getStartAndEnd((Integer) year, (Integer) month);
		long start = period[0].getTime() / (24 * 60 * 60 * 1000);
		long end = period[1].getTime() / (24 * 60 * 60 * 1000);

		BasicDBObject startCondition = new BasicDBObject().append(
				WorksPerformence.F_DATECODE,
				new BasicDBObject().append("$gte", new Long(start)));
		BasicDBObject endCondition = new BasicDBObject().append(
				WorksPerformence.F_DATECODE,
				new BasicDBObject().append("$lte", new Long(end)));

		query.put("$and", new BasicDBObject[] { startCondition, endCondition });

		DBObject fields = new BasicDBObject();
		fields.put(WorksPerformence.F_DATECODE, 1);
		fields.put(WorksPerformence.F_WORKS, 1);
		fields.put(WorksPerformence.F_PROJECT_ID, 1);

		DBCursor cur = workPerformenceCol.find(query, fields);
		while (cur.hasNext()) {
			DBObject data = cur.next();
			Object projectId = data.get(WorksPerformence.F_PROJECT_ID);
			if (projectId == null) {
				continue;
			}

			// ȡ��ʱ
			Object works = data.get(WorksPerformence.F_WORKS);
			if (!(works instanceof Number)) {
				continue;
			}

			// 4. ���ݹ�ʱ��¼����Ŀid������ѯ�������
			DBObject projectWorkOrderData = projectCol.findOne(
					new BasicDBObject().append(Project.F__ID, projectId),
					new BasicDBObject().append(Project.F_WORK_ORDER, 1));
			if (projectWorkOrderData == null) {
				continue;
			}

			Object workOrders = projectWorkOrderData.get(Project.F_WORK_ORDER);
			if (!(workOrders instanceof List<?>)) {
				continue;
			}

			// Ӧ�����������ɱ����ĵĹ������
			Set<String> effectiveWorkOrders = new HashSet<String>();

			List<?> list = (List<?>) workOrders;
			// ������ֶ���������ŵģ�������֯������ŵĶ�Ӧ�Ƿ���ȷ������Ǹóɱ����ĵĹ�����ţ�����ʱ���ݱ������ù������
			for (int i = 0; i < list.size(); i++) {
				String workOrder = (String) list.get(i);
				if (company.hasWorkOrder(workOrder)) {
					effectiveWorkOrders.add(workOrder);
				}
			}
			// ����ʱƽ̯���������
			double worksPerOrder = ((Double) works)
					/ (effectiveWorkOrders.size());
			Iterator<String> iter = effectiveWorkOrders.iterator();
			while (iter.hasNext()) {
				String workOrder = iter.next();

				// �ۼƵ���ʱ��¼��
				Double value = workOrderMapWorks.get(workOrder);
				if (value == null) {
					value = worksPerOrder;
				} else {
					value = value.doubleValue() + worksPerOrder;
				}
				workOrderMapWorks.put(workOrder, value);
			}
		}

		// ȫ��������,�����ܹ�ʱ��
		Iterator<Double> iterator = workOrderMapWorks.values().iterator();
		double total = 0d;
		while (iterator.hasNext()) {
			total += iterator.next();
		}

		List<DBObject> toBeInsert = new ArrayList<DBObject>();

		Iterator<String> iter = workOrderMapWorks.keySet().iterator();
		while (iter.hasNext()) {
			String workOrderNumber = iter.next();
			DBObject wopc = new BasicDBObject();
			wopc.put(WorkOrderPeriodCost.F_WORKORDER, workOrderNumber);
			wopc.put(WorkOrderPeriodCost.F_YEAR, year);
			wopc.put(WorkOrderPeriodCost.F_MONTH, month);
			wopc.put(WorkOrderPeriodCost.F_COSTCENTERCODE, costCenterCode);

			// ���湤ʱ��������
			// ��øù�����ŵ��ۼƹ�ʱ
			Double works = workOrderMapWorks.get(workOrderNumber);
			double ratio = works / total;

			Iterator<String> iter2 = rndpc.get_data().keySet().iterator();
			while (iter2.hasNext()) {
				String key = iter.next();
				if (!Utils.isNumbers(key)) {// �����������͵��ֶκ���
					continue;
				}
				Object cost = rndpc.getValue(key);
				if (cost == null) {
					wopc.put(key, 0d);
				} else {
					double value = ((Double) cost) * ratio;
					wopc.put(key, value);
				}
			}

			toBeInsert.add(wopc);
		}

		costAllocateCol.insert(toBeInsert);

		//׼������
		ArrayList<WorkOrderPeriodCost> result = new ArrayList<WorkOrderPeriodCost>();
		for (int i = 0; i < toBeInsert.size(); i++) {
			result.add(ModelService.createModelObject(toBeInsert.get(i),
					WorkOrderPeriodCost.class));
		}

		return result;
	}

	/**
	 * ��õ�ǰ��֯�����е�Ա��id, ���������¼��ǳɱ����ĵ���֯
	 * 
	 * @param org
	 * @return
	 */
	private Set<String> getCostCenterUserIdList(Organization org) {
		Set<String> result = new HashSet<String>();
		List<String> ids = org.getMemberIds(false);
		result.addAll(ids);

		List<PrimaryObject> childrenOrgs = org.getChildrenOrganization();
		for (int i = 0; i < childrenOrgs.size(); i++) {
			Organization childOrg = (Organization) childrenOrgs.get(i);
			if (childOrg.isCostCenter()) {
				continue;
			}
			result.addAll(getCostCenterUserIdList(childOrg));
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
		cal.add(Calendar.MILLISECOND, -1);
		Date end = cal.getTime();
		return new Date[] { start, end };
	}

}
