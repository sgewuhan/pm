package com.sg.business.finance.schedual;

import java.util.Calendar;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.commons.eai.RNDPeriodCostAdapter;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

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
		
		String[] costCodeArray = new String[cur.size()];
		int i =0;
		while(cur.hasNext()){
			DBObject next = cur.next();
			costCodeArray[i] = (String) next.get(Organization.F_COST_CENTER_CODE);
			i++;
		}
		
		try {
			adapter.runGetData(null, costCodeArray, cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH) + 1, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
