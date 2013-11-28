package com.sg.business.finance.schedual;

import java.util.Calendar;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;
import com.sg.sqldb.utility.IRowCallBack;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;

public class RunIncomeAllocation implements ISchedualJobRunnable {
	@Override
	public boolean run() throws Exception {
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i > -23; i--) {

			long start = System.currentTimeMillis();
			String year = "" + cal.get(Calendar.YEAR);
			String month = String.format("%03d", (cal.get(Calendar.MONTH)));
			cal.add(Calendar.MONTH, -1);

			try {
				Commons.LOGGER.info("准备获取SAP销售数据:" + year + "-" + month);
				doTest(year, month);
			} catch (Exception e) {
				Commons.LOGGER.error("获得SAP销售数据失败:" + year + "-" + month, e);
				throw e;
			}
			long end = System.currentTimeMillis();
			Commons.LOGGER.info("获得SAP销售数据完成:" + year + "-" + month + " "
					+ (end - start) / 1000);
		}

		return true;
	}

	private void doTest(String year, String month) {
		try {
			final DBCollection col = DBActivator.getCollection(
					IModelConstants.DB_BI, "salesdata");

			IRowCallBack callback = new IRowCallBack() {

				@Override
				public void input(SQLRow row) {
					BasicDBObject dbo = new BasicDBObject();
					for (int i = 0; i < row.getColumns().length; i++) {
						Object v = row.getValue(row.getColumns()[i]);
						Object value = Utils.getJSONValueFromSQL(v);
						dbo.put(row.getColumns()[i], value);
					}
					col.insert(dbo);
				}
			};
			SQLUtil.SQL_QUERY(
					"sap",
					"select PALEDGER,PERDE ,MATNR ,VV010,VV030,VV040,BUKRS,BZIRK,KNDNR,VKBUR,VKGRP,VKORG "
							+ "From SAPSR3.CE14000 "
							+ "WHERE mandt = '700' and paledger = '02' and gjahr = '"
							+ year + "' and PERDE = " + month + "", callback);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
