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

		// 获得所有的成本中心代码
		String[] costCodes = getCostCodeArray(year, month);
		// 获取成本元素代码
		String[] costElementArray = CostAccount.getCostElemenArray();
		// 获取工作令号
		String[] workOrders = CompanyWorkOrder.getWorkOrders();
		String message = ""; //$NON-NLS-1$
		while (i != 0) {
			try {
				clear(year, month);
				return doETL(year, month, day, costElementArray, workOrders,
						costCodes);
			} catch (Exception e) {
				i--;
				message += e.getMessage() + "\n"; //$NON-NLS-1$

			}
		}
		throw new Exception(message);
	}

	protected boolean doETL(int year, int month, int day,
			String[] costElementArray, String[] workOrders, String[] costCodes)
			throws Exception {
		long start, end;

		Commons.loginfo("[成本数据]准备获取SAP成本中心数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
		start = System.currentTimeMillis();
		RNDPeriodCostAdapter rndAdapter = new RNDPeriodCostAdapter();
		rndAdapter.runGetData(costCodes, costElementArray, year, month);
		end = System.currentTimeMillis();
		Commons.loginfo("[成本数据]获得SAP成本中心数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ (end - start) / 1000 + " S");

		Commons.loginfo("[成本数据]准备获取SAP工作令号研发成本数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
		start = System.currentTimeMillis();
		WorkorderPeriodCostAdapter workorderadapter = new WorkorderPeriodCostAdapter();
		workorderadapter.runGetData(workOrders, costElementArray, year, month);
		end = System.currentTimeMillis();
		Commons.loginfo("[成本数据]获得SAP工作令号研发成本完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ (end - start) / 1000 + " S"); //$NON-NLS-1$

		Commons.loginfo("[销售数据]准备获取SAP销售数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
		start = System.currentTimeMillis();
		runGetData(year, month);
		end = System.currentTimeMillis();
		Commons.loginfo("[销售数据]获得SAP销售数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ (end - start) / 1000 + " S");

		Commons.loginfo("[销售数据]准备更新项目销售数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
		start = System.currentTimeMillis();
		ProjectToolkit.updateProjectSalesData();
		end = System.currentTimeMillis();
		Commons.loginfo("[销售数据]更新项目销售数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ (end - start) / 1000 + " S");

		Commons.loginfo("[项目数据]准备更新项目ETL数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
		start = System.currentTimeMillis();
		List<DBObject> projectETLList = doProjectETL(year, month, day);
		end = System.currentTimeMillis();
		Commons.loginfo("[项目数据]更新项目ETL数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ (end - start) / 1000 + " S");

		if (day == 1) {
			Commons.loginfo("[项目数据]准备更新项目月ETL数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
			start = System.currentTimeMillis();
			projectMonthCol.remove(new BasicDBObject().append(
					IProjectETL.F_YEAR, year)
					.append(IProjectETL.F_MONTH, month));
			projectMonthCol.insert(projectETLList, WriteConcern.NORMAL);
			end = System.currentTimeMillis();
			Commons.loginfo("[项目数据]更新项目月ETL数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ (end - start) / 1000 + " S");
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
		cal.add(Calendar.MILLISECOND, -1);

		BasicDBObject query = getProjectETLQuery();
		DBCursor cur = pjCol.find(query);
		while (cur.hasNext()) {
			DBObject dbObject = (DBObject) cur.next();
			ObjectId id = (ObjectId) dbObject.get(Project.F__ID);
			if (id != null) {
				Project project = ModelService.createModelObject(Project.class,
						id);
				if (day == 1) {
					ProjectMonthlyETL pres = project.getMonthlyETL();
					DBObject etl = pres.doETL(cal);
					projectETLList.add(etl);
				} else {
					ProjectETL pres = project.getETL();
					pres.doETL(cal);
				}
				if (Portal.getDefault().isDevelopMode()) {
					Commons.loginfo(project.getLabel() + " ETL finished."); //$NON-NLS-1$
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

	protected abstract BasicDBObject getProjectETLQuery();

	private String[] getCostCodeArray(int year, int month) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		List<?> distinct = col
				.distinct(
						Organization.F_COST_CENTER_CODE,
						new BasicDBObject().append(
								"$and", //$NON-NLS-1$
								new BasicDBObject[] {
										new BasicDBObject()
												.append(Organization.F_COST_CENTER_CODE,
														new BasicDBObject()
																.append("$ne", //$NON-NLS-1$
																		null)),
										new BasicDBObject()
												.append(Organization.F_COST_CENTER_CODE,
														new BasicDBObject()
																.append("$ne", //$NON-NLS-1$
																		"")) })); //$NON-NLS-1$
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
		String gjahr = "" + year; //$NON-NLS-1$
		String perde = String.format("%03d", month); //$NON-NLS-1$
		Connection connection = DDB.getDefault().createConnection("sap"); //$NON-NLS-1$
		try {
			SQLUtil.SQL_QUERY(
					"sap", //$NON-NLS-1$
					"select PALEDGER,GJAHR,PERDE ,MATNR ,VV010,VV030,VV040,BUKRS,BZIRK,KNDNR,VKBUR,VKGRP,VKORG " //$NON-NLS-1$
							+ "From SAPSR3.CE14000 " //$NON-NLS-1$
							+ "WHERE MANDT = '700' and PALEDGER = '02' and GJAHR = '" //$NON-NLS-1$
							+ gjahr + "' and PERDE = " + perde + "", callback, //$NON-NLS-1$ //$NON-NLS-2$
					connection);
		} catch (Exception e) {
		} finally {
			connection.close();
		}

	}

	private void rndClear(int year, int month) {
		rndCol.remove(new BasicDBObject().append("year", year).append("month", //$NON-NLS-1$ //$NON-NLS-2$
				month));
		rndAllocationCol.remove(new BasicDBObject().append("year", year) //$NON-NLS-1$
				.append("month", month)); //$NON-NLS-1$
	}

	private void workOrderClear(int year, int month) {
		workOrderCol.remove(new BasicDBObject().append("year", year).append( //$NON-NLS-1$
				"month", month)); //$NON-NLS-1$
	}

	private void saleDataClear(int year, int month) {
		String gjahr = "" + year; //$NON-NLS-1$
		String perde = String.format("%03d", month); //$NON-NLS-1$
		saleDataCol.remove(new BasicDBObject().append("GJAHR", gjahr).append( //$NON-NLS-1$
				"PERDE", perde)); //$NON-NLS-1$
	}

}
