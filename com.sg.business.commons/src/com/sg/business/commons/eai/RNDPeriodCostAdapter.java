package com.sg.business.commons.eai;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.commons.eai.sap.JCO_ZXFUN_PM_YFFY;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.RNDPeriodCost;

public class RNDPeriodCostAdapter {

	public static final String ORGCODE = "org";
	public static final String COSECENTERCODE = "cost";
	public static final String ACCOUNTNUMERS = "account";
	public static final String YEAR = "year";
	public static final String MONTH = "month";

	public RNDPeriodCost getData(Map<String, Object> parameter)
			throws Exception {
		Object year = parameter.get(YEAR);
		Object month = parameter.get(MONTH);
		if (!(year instanceof Integer) || !(month instanceof Integer)) {
			throw new IllegalArgumentException("�ڼ� year, month��������");
		}

		Object org = parameter.get(ORGCODE);
		if (!(org instanceof String)) {
			throw new IllegalArgumentException("��֯���� org ��������");
		}

		Object cost = parameter.get(COSECENTERCODE);
		if (!(cost instanceof String)) {
			throw new IllegalArgumentException("�ɱ����Ĵ��� costcode ��������");
		}

		Object account = parameter.get(ACCOUNTNUMERS);
		if (account != null && !(account instanceof String[])) {
			throw new IllegalArgumentException("��Ŀ��  account ��������");
		}

		RNDPeriodCost[] result = runGetData(new String[] { (String) org },
				new String[] { (String) cost }, (int) year, (int) month,
				(String[]) account);

		return result[0];
	}

	// public Date[] getStartAndEnd(Integer year, Integer month) {
	// Calendar cal = Calendar.getInstance();
	// cal.set(Calendar.YEAR, year);
	// cal.set(Calendar.MONTH, month - 1);
	// cal.set(Calendar.DATE, 1);
	// cal.set(Calendar.HOUR_OF_DAY, 0);
	// cal.set(Calendar.MINUTE, 0);
	// cal.set(Calendar.SECOND, 0);
	// cal.set(Calendar.MILLISECOND, 0);
	// Date start = cal.getTime();
	// cal.add(Calendar.MILLISECOND, -1);
	// Date end = cal.getTime();
	// return new Date[] { start, end };
	// }

	public String[] getDefaultAccounts() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_COSTACCOUNT_ITEM);
		DBCursor cur = col.find();
		int i = 0;
		String[] ret = new String[cur.size()];

		while (cur.hasNext()) {
			DBObject next = cur.next();
			ret[i++] = (String) next.get(CostAccount.accountnumber);
		}
		return ret;
	}

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
	 * @return
	 * @throws Exception
	 */
	public RNDPeriodCost[] runGetData(String[] orgCodeArray,
			String[] costCodeArray, int year, int month, String[] account)
			throws Exception {
		if (account == null) {
			account = getDefaultAccounts();
		}

		RNDPeriodCost[] result = new RNDPeriodCost[orgCodeArray.length];

		JCO_ZXFUN_PM_YFFY func = new JCO_ZXFUN_PM_YFFY();
		Map<String, Map<String, Double>> ret = func.getJSDZB(orgCodeArray,
				costCodeArray, year, month, account);

		// ���´���ģ���Ѿ������SAP������
		DBObject[] sr = new BasicDBObject[ret.size()];
		Iterator<String> iter = ret.keySet().iterator();
		int i=0;
		while(iter.hasNext()){
			String costCenterCode = iter.next();
			sr[i] = new BasicDBObject();
			sr[i].put(RNDPeriodCost.F_COSTCENTERCODE, costCenterCode);
			sr[i].put(RNDPeriodCost.F_MONTH, new Integer(month));
			sr[i].put(RNDPeriodCost.F_YEAR, new Integer(year));
			sr[i].put(RNDPeriodCost.F__CDATE, new Date());
			Map<String, Double> values = ret.get(costCenterCode);
			if(values!=null){
				sr[i].putAll(values);
			}
			i++;
		}
		

		// ���浽���ݿ�
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		col.insert(sr, WriteConcern.NORMAL);
		
		for (int j = 0; j < sr.length; j++) {
			result[j] = ModelService.createModelObject(sr[j],RNDPeriodCost.class );
		}
		return result;
	}

}
