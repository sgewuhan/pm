package com.sg.business.finance.schedual;

import com.sg.sqldb.utility.SQLUtil;

public class RunIncomeAllocation implements Runnable {

	@Override
	public void run() {
//		String sql;
		try {
			SQLUtil.SQL_QUERY("SAP", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
