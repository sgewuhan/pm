package com.sg.business.model.etl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.RNDPeriodCost;
import com.sg.business.model.etl.eai.RNDPeriodCostAdapter;

/**
 * 每月1号对上月的数据进行获取
 * 
 * @author Administrator
 * 
 */
public class RNDCostAllocationETLJob implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		RNDPeriodCostAdapter adapter = new RNDPeriodCostAdapter();

		Calendar cal = Calendar.getInstance();

		// for (int i = 0; i > -23; i--) {
		cal.add(Calendar.MONTH, -1);

		long start = System.currentTimeMillis();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		// 获得所有的成本中心代码
		String[] costCodes = getCostCodeArray(year, month);
		String[] costElementArray = CostAccount.getCostElemenArray();
		try {
			Commons.loginfo("[成本数据]准备获取SAP成本中心数据:" + year + "-" + month);
			adapter.runGetData(costCodes, costElementArray, year, month);
		} catch (Exception e) {
			Commons.logerror("[成本数据]获得SAP成本中心数据失败:" + year + "-" + month, e);
			throw e;
		}
		long end = System.currentTimeMillis();
		Commons.loginfo("[成本数据]获得SAP成本中心数据完成:" + year + "-" + month + " "
				+ (end - start) / 1000);
		// }

		return true;

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
			// 检查该成本中心是否已经取数
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
