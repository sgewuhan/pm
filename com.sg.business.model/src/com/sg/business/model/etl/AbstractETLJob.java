package com.sg.business.model.etl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.model.CompanyWorkOrder;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.etl.eai.RNDPeriodCostAdapter;
import com.sg.business.model.etl.eai.WorkorderPeriodCostAdapter;
import com.sg.business.model.toolkit.ProjectToolkit;
import com.sg.sqldb.DDB;
import com.sg.sqldb.utility.IRowCallBack;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;

public abstract class AbstractETLJob implements ISchedualJobRunnable {

	private DBCollection rndCol;
	private DBCollection workOrderCol;
	private DBCollection saleDataCol;
	private DBCollection rndAllocationCol;
	private DBCollection pjCol;
	private DBCollection projectMonthCol;

	@Override
	public boolean run() throws Exception {
		initCollection();
		int year = getYear();
		int month = getMonth();
		int day = getDay();
		int i = 5;

		// ������еĳɱ����Ĵ���
		String[] costCodes = getCostCodeArray(year, month);
		// ��ȡ�ɱ�Ԫ�ش���
		String[] costElementArray = CostAccount.getCostElemenArray();
		// ��ȡ�������
		String[] workOrders = CompanyWorkOrder.getWorkOrders();
		String message = "";
		while (i != 0) {
			try {
				clear(year, month);
				return doETL(year, month, day, costElementArray, workOrders,
						costCodes);
			} catch (Exception e) {
				i--;
				message += e.getMessage() + "\n";

			}
		}
		throw new Exception(message);
	}

	protected boolean doETL(int year, int month, int day,
			String[] costElementArray, String[] workOrders, String[] costCodes)
			throws Exception {
		long start, end;

		Commons.loginfo("[�ɱ�����]׼����ȡSAP�ɱ���������:" + year + "-" + month);
		start = System.currentTimeMillis();
		RNDPeriodCostAdapter rndAdapter = new RNDPeriodCostAdapter();
		rndAdapter.runGetData(costCodes, costElementArray, year, month);
		end = System.currentTimeMillis();
		Commons.loginfo("[�ɱ�����]���SAP�ɱ������������:" + year + "-" + month + " "
				+ (end - start) / 1000);

		Commons.loginfo("[�ɱ�����]׼����ȡSAP��������з��ɱ�����:" + year + "-" + month);
		start = System.currentTimeMillis();
		WorkorderPeriodCostAdapter workorderadapter = new WorkorderPeriodCostAdapter();
		workorderadapter.runGetData(workOrders, costElementArray, year, month);
		end = System.currentTimeMillis();
		Commons.loginfo("[�ɱ�����]���SAP��������з��ɱ����:" + year + "-" + month + " "
				+ (end - start) / 1000 + "S");

		Commons.loginfo("[��������]׼����ȡSAP��������:" + year + "-" + month);
		start = System.currentTimeMillis();
		runGetData(year, month);
		end = System.currentTimeMillis();
		Commons.loginfo("[��������]���SAP�����������:" + year + "-" + month + " "
				+ (end - start) / 1000);

		Commons.loginfo("[��������]׼��������Ŀ��������:" + year + "-" + month);
		start = System.currentTimeMillis();
		ProjectToolkit.updateProjectSalesData(year, month, day);
		end = System.currentTimeMillis();
		Commons.loginfo("[��������]������Ŀ�����������:" + year + "-" + month + " "
				+ (end - start) / 1000);

		Commons.loginfo("[��Ŀ����]׼��������ĿETL����:" + year + "-" + month);
		start = System.currentTimeMillis();
		List<DBObject> projectETLList = doProjectETL(year, month, day);
		end = System.currentTimeMillis();
		Commons.loginfo("[��Ŀ����]������ĿETL�������:" + year + "-" + month + " "
				+ (end - start) / 1000);

		if (day == 1) {
			Commons.loginfo("[��Ŀ����]׼��������Ŀ��ETL����:" + year + "-" + month);
			start = System.currentTimeMillis();
			projectMonthCol.remove(new BasicDBObject().append(IProjectETL.F_YEAR, year).append(IProjectETL.F_MONTH,
					month));
			projectMonthCol.insert(projectETLList, WriteConcern.NORMAL);
			end = System.currentTimeMillis();
			Commons.loginfo("[��Ŀ����]������Ŀ��ETL�������:" + year + "-" + month + " "
					+ (end - start) / 1000);
		}

		return true;
	}

	public List<DBObject> doProjectETL(int year, int month, int day)
			throws Exception {
		List<DBObject> projectETLList = new ArrayList<DBObject>();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.MILLISECOND, -1);

		BasicDBObject query = getProjectETLQuery();
		DBCursor cur = pjCol.find(query);
		while (cur.hasNext()) {
			DBObject dbObject = (DBObject) cur.next();
			ObjectId id = (ObjectId) dbObject.get(Project.F__ID);
			if (id != null) {
				Project project = ModelService.createModelObject(Project.class,
						id);
				ProjectETL pres = project.getETL();
				DBObject etl = pres.doETL(cal);
				projectETLList.add(etl);
				if (Portal.getDefault().isDevelopMode()) {
					Commons.loginfo(project.getLabel() + " ETL finished.");
				}
			}
		}
		return projectETLList;
	}

	private void clear(int year, int month) {
		rndClear(year, month);
		workOrderClear(year, month);
		saleDataClear(year, month);
	}

	private void initCollection() {
		rndCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		rndAllocationCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		workOrderCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORKORDER_COST);
		saleDataCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_SALESDATA);
		pjCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT);
		projectMonthCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_PROJECT_MONTH_DATA);
	}

	protected abstract int getMonth();

	protected abstract int getYear();

	protected abstract int getDay();

	protected abstract  BasicDBObject getProjectETLQuery();

	private String[] getCostCodeArray(int year, int month) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		List<?> distinct = col
				.distinct(
						Organization.F_COST_CENTER_CODE,
						new BasicDBObject().append(
								"$and",
								new BasicDBObject[] {
										new BasicDBObject()
												.append(Organization.F_COST_CENTER_CODE,
														new BasicDBObject()
																.append("$ne",
																		null)),
										new BasicDBObject()
												.append(Organization.F_COST_CENTER_CODE,
														new BasicDBObject()
																.append("$ne",
																		"")) }));
		return (String[]) distinct.toArray(new String[0]);

	}

	public void runGetData(int year, int month) throws Exception {
		IRowCallBack callback = new IRowCallBack() {

			@Override
			public void input(SQLRow row) {
				BasicDBObject dbo = new BasicDBObject();
				for (int i = 0; i < row.getColumns().length; i++) {
					Object v = row.getValue(row.getColumns()[i]);
					Object value = Utils.getJSONValueFromSQL(v);
					dbo.put(row.getColumns()[i], value);
				}
				saleDataCol.insert(dbo);
			}
		};
		String gjahr = "" + year;
		String perde = String.format("%03d", month);
		Connection connection = DDB.getDefault().createConnection("sap");
		try {
			SQLUtil.SQL_QUERY(
					"sap",
					"select PALEDGER,GJAHR,PERDE ,MATNR ,VV010,VV030,VV040,BUKRS,BZIRK,KNDNR,VKBUR,VKGRP,VKORG "
							+ "From SAPSR3.CE14000 "
							+ "WHERE MANDT = '700' and PALEDGER = '02' and GJAHR = '"
							+ gjahr + "' and PERDE = " + perde + "", callback,
					connection);
		} catch (Exception e) {
		} finally {
			connection.close();
		}

	}

	private void rndClear(int year, int month) {
		rndCol.remove(new BasicDBObject().append("year", year).append("month",
				month));
		rndAllocationCol.remove(new BasicDBObject().append("year", year)
				.append("month", month));
	}

	private void workOrderClear(int year, int month) {
		workOrderCol.remove(new BasicDBObject().append("year", year).append(
				"month", month));
	}

	private void saleDataClear(int year, int month) {
		String gjahr = "" + year;
		String perde = String.format("%03d", month);
		saleDataCol.remove(new BasicDBObject().append("GJAHR", gjahr).append(
				"PERDE", perde));
	}

}
