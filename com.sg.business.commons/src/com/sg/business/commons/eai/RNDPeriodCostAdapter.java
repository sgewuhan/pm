package com.sg.business.commons.eai;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.RNDPeriodCost;

public class RNDPeriodCostAdapter {

	public static final String ORGCODE = "org";
	public static final String COSECENTERCODE = "cost";
	public static final String ACCOUNTNUMERS = "account";
	public static final String YEAR = "year";
	public static final String MONTH = "month";

	public RNDPeriodCost getData(Map<String, Object> parameter) {
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
		if (account!=null&&!(account instanceof String[])) {
			throw new IllegalArgumentException("��Ŀ��  account ��������");
		}

		RNDPeriodCost[] result = runGetData(new String[] { (String) org },
				new String[] { (String) cost }, (int) year, (int) month, (String[])account);

		return result[0];
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
	 */
	public RNDPeriodCost[] runGetData(String[] orgCodeArray,
			String[] costCodeArray, int year, int month, String[] account) {
		if (account == null) {
			account = getDefaultAccounts();
		}

		RNDPeriodCost[] result = new RNDPeriodCost[orgCodeArray.length];
		
		// ���´���ģ���Ѿ������SAP������
		DBObject[] sr = new BasicDBObject[orgCodeArray.length];
		for (int i = 0; i < sr.length; i++) {
			sr[i] = new BasicDBObject();
			sr[i].put(RNDPeriodCost.F_COSTCENTERCODE, costCodeArray[i]);
			sr[i].put(RNDPeriodCost.F_MONTH, new Integer(month));
			sr[i].put(RNDPeriodCost.F_YEAR, new Integer(year));
			sr[i].put(RNDPeriodCost.F__CDATE, new Date());
			for (int j = 0; j < account.length; j++) {
				double value = (Math.random() * 100000);
				sr[i].put(account[j], new Double(value));
			}
			
			result[i] = ModelService.createModelObject(sr[i], RNDPeriodCost.class);
		}

		
		
		// ���浽���ݿ�
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		col.insert(sr, WriteConcern.NORMAL);
		return result;
	}

}
