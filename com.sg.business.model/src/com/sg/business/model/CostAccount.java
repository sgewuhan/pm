package com.sg.business.model;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class CostAccount extends PrimaryObject {

	public static final String F_COST_ACCOUNTNUMBER = "accountnumber";
	
	/**
	 * 获取成本元素代码
	 * @return
	 */
	public static  String[] getCostElemenArray() {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_COSTACCOUNT_ITEM);
		DBCursor cur = col.find();
		String[] result = new String[cur.size()];
		int i = 0;
		while (cur.hasNext()) {
			result[i++] = (String) cur.next().get(
					CostAccount.F_COST_ACCOUNTNUMBER);
		}
		return result;
	}


}
