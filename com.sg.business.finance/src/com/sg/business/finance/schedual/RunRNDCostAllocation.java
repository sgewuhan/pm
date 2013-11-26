package com.sg.business.finance.schedual;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.mobnut.commons.Commons;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.finance.eai.RNDPeriodCostAdapter;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.RNDPeriodCost;

/**
 * ÿ��1�Ŷ����µ����ݽ��л�ȡ
 * 
 * @author Administrator
 * 
 */
public class RunRNDCostAllocation implements Runnable {

	@Override
	public void run() {
		RNDPeriodCostAdapter adapter = new RNDPeriodCostAdapter();

		Calendar cal = Calendar.getInstance();

//		for (int i = -1; i > -25; i--) {
			cal.add(Calendar.MONTH, -1);

			long start = System.currentTimeMillis();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;

			// ������еĳɱ����Ĵ���
			String[] costCodes = getCostCodeArray(year, month);

			try {
				Commons.LOGGER.info("׼����ȡSAP�ɱ���������:" + year + "-" + month);
				adapter.runGetData(null, costCodes, year, month, null);
			} catch (Exception e) {
				Commons.LOGGER.error("���SAP�ɱ���������ʧ��:" + year + "-" + month, e);
			}
			long end = System.currentTimeMillis();
			Commons.LOGGER.info("���SAP�ɱ������������:" + year + "-" + month + " "
					+ (end - start) / 1000);
//		}

	}

	private String[] getCostCodeArray(int year, int month) {
		Set<String> costCodeArray = new HashSet<String>();
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBCursor cur = col.find(new BasicDBObject().append(
				"$and",
				new BasicDBObject[] {
						new BasicDBObject().append(
								Organization.F_COST_CENTER_CODE,
								new BasicDBObject().append("$ne", null)),
						new BasicDBObject().append(
								Organization.F_COST_CENTER_CODE,
								new BasicDBObject().append("$ne", "")) }),
				new BasicDBObject().append(Organization.F_COST_CENTER_CODE, 1));

		DBCollection rndcostCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		while (cur.hasNext()) {
			DBObject next = cur.next();
			String costCode = (String) next
					.get(Organization.F_COST_CENTER_CODE);
			// ���óɱ������Ƿ��Ѿ�ȡ��
			long cnt = rndcostCol.count(new BasicDBObject()
					.append(RNDPeriodCost.F_COSTCENTERCODE, costCode)
					.append(RNDPeriodCost.F_YEAR, new Integer(year))
					.append(RNDPeriodCost.F_MONTH, new Integer(month)));
			if (cnt == 0) {
				costCodeArray.add(costCode);
			}

		}
		return costCodeArray.toArray(new String[0]);

	}
}
