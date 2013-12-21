package com.sg.business.model.etl.eai;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkOrderPeriodCost;

public class BasicPeriodCostAdapter {

	public static final String ORGCODE = "org"; //$NON-NLS-1$
	public static final String COSECENTERCODE = "cost"; //$NON-NLS-1$
	public static final String ACCOUNTNUMERS = "account"; //$NON-NLS-1$
	public static final String YEAR = "year"; //$NON-NLS-1$
	public static final String MONTH = "month"; //$NON-NLS-1$


	/**
	 * ���SAP�ɱ������ڼ��з��ɱ�������
	 * 
	 * @param orgCodeArray
	 *            , ��֯�������飬��Ӧ�ڳɱ����Ĵ�������
	 * @param costCodeArray
	 *            ���ɱ����Ĵ������飬��Ӧ����֯��������
	 * @param start
	 *            , ������ڼ俪ʼʱ��
	 * @param end
	 *            , �ڼ����ʱ��
	 * @param account
	 *            , �з��ɱ���Ŀ, Ϊ��ʱȡȫ����Ŀ
	 * @param costElementArray
	 * @param targetCollection
	 * @throws Exception
	 */
	public DBObject[] runGetData(String[] costCodeArray,
			String[] workordersArray, String[] costElementArray, int year,
			int month, String targetCollection)
			throws Exception {
		JCO_ZXFUN_PM_YFFY func = new JCO_ZXFUN_PM_YFFY();
		Map<String, Map<String, Double>> ret = func.getCost(costCodeArray,
				workordersArray, costElementArray, year, month);

		DBObject[] sr = new BasicDBObject[ret.size()];
		Iterator<String> iter = ret.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String costCenterCode = iter.next();
			sr[i] = new BasicDBObject();
			sr[i].put(WorkOrderPeriodCost.F_WORKORDER, costCenterCode);
			sr[i].put(WorkOrderPeriodCost.F_COSTCENTERCODE, costCenterCode);
			sr[i].put(WorkOrderPeriodCost.F_MONTH, new Integer(month));
			sr[i].put(WorkOrderPeriodCost.F_YEAR, new Integer(year));
			sr[i].put(WorkOrderPeriodCost.F__CDATE, new Date());
			Map<String, Double> values = ret.get(costCenterCode);
			if (values != null) {
				sr[i].putAll(values);
			}
			i++;
		}

		// ���浽���ݿ�
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				targetCollection);
		col.insert(sr, WriteConcern.NORMAL);

		return sr;
	}

}
