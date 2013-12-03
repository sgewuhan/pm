package com.sg.business.finance.schedual;

import java.util.Calendar;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.sqldb.utility.IRowCallBack;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;

public class RunIncomeAllocation implements ISchedualJobRunnable {
	private DBCollection col;

	@Override
	public boolean run() throws Exception {
		col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SALESDATA);

		Calendar cal = Calendar.getInstance();
		// for (int i = 0; i > -23; i--) {
		cal.add(Calendar.MONTH, -1);

		long start = System.currentTimeMillis();
		String year = "" + cal.get(Calendar.YEAR);
		String month = String.format("%03d", cal.get(Calendar.MONTH)+1);

		// �����������
		clear(year, month);

		try {
			Commons.loginfo("[��������]׼����ȡSAP��������:" + year + "-" + month);
			runGetData(year, month);

		} catch (Exception e) {
			Commons.logerror("[��������]���SAP��������ʧ��:" + year + "-" + month, e);
			throw e;
		}
		long end = System.currentTimeMillis();
		Commons.loginfo("[��������]���SAP�����������:" + year + "-" + month + " "
				+ (end - start) / 1000);
		// }

		ProjectToolkit.updateProjectSalesData();
		return true;
	}

	private void clear(String year, String month) {
		col.remove(new BasicDBObject().append("GJAHR", year).append("PERDE",
				month));
	}

	public void runGetData(String year, String month) throws Exception {
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
				"select PALEDGER,GJAHR,PERDE ,MATNR ,VV010,VV030,VV040,BUKRS,BZIRK,KNDNR,VKBUR,VKGRP,VKORG "
						+ "From SAPSR3.CE14000 "
						+ "WHERE MANDT = '700' and PALEDGER = '02' and GJAHR = '"
						+ year + "' and PERDE = " + month + "", callback);

	}
}
