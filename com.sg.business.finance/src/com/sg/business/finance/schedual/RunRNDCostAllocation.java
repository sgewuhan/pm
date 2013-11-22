package com.sg.business.finance.schedual;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

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
 * 每月1号对上月的数据进行获取
 * 
 * @author Administrator
 * 
 */
public class RunRNDCostAllocation implements Runnable {

	@Override
	public void run() {
		RNDPeriodCostAdapter adapter = new RNDPeriodCostAdapter();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		// 获得所有的成本中心代码
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

		Set<String> costCodeArray = new HashSet<String>();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
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

		try {

			adapter.runGetData(null, costCodeArray.toArray(new String[0]),
					year, month, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
