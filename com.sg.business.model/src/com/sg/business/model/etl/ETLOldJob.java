package com.sg.business.model.etl;

import java.util.Calendar;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.CompanyWorkOrder;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.etl.eai.WorkorderPeriodCostAdapter;
import com.sg.business.model.toolkit.ProjectToolkit;

public class ETLOldJob implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		//2009-1-1 58
		final ObjectId org_id = new ObjectId("");
		final Organization org = ModelService.createModelObject(Organization.class, org_id);
		
		for (int i = 0; i >= -1; i--) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.DATE, -1);

			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH) + 1;
			final int day = 1;
			System.out.println("" + year + "-" + month + "-" + day); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			AbstractETLJob etl = new AbstractETLJob() {

				@Override
				protected boolean doETL(int year, int month, int day,
						String[] costElementArray, String[] workOrders,
						String[] costCodes) throws Exception {
					long start, end;

					Commons.loginfo("[项目数据]准备更新项目ETL数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
					start = System.currentTimeMillis();
					List<DBObject> projectETLList = doProjectETL(year, month, day);
					end = System.currentTimeMillis();
					Commons.loginfo("[项目数据]更新项目ETL数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ (end - start) / 1000 + " S");

					if (day == 1) {
						Commons.loginfo("[项目数据]准备更新项目月ETL数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
						start = System.currentTimeMillis();
						doProjectMonthETL(year, month, projectETLList);
						end = System.currentTimeMillis();
						Commons.loginfo("[项目数据]更新项目月ETL数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								+ (end - start) / 1000 + " S");
					}

//					Commons.loginfo("[成本数据]准备获取SAP成本中心数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
//					start = System.currentTimeMillis();
//					RNDPeriodCostAdapter rndAdapter = new RNDPeriodCostAdapter();
//					rndAdapter.runGetData(costCodes, costElementArray, year, month);
//					end = System.currentTimeMillis();
//					Commons.loginfo("[成本数据]获得SAP成本中心数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//							+ (end - start) / 1000 + " S");

					Commons.loginfo("[成本数据]准备获取SAP工作令号研发成本数据:" + year + "-" + month); //$NON-NLS-1$ //$NON-NLS-2$
					start = System.currentTimeMillis();
					WorkorderPeriodCostAdapter workorderadapter = new WorkorderPeriodCostAdapter();
					workorderadapter.runGetData(CompanyWorkOrder.getWorkOrders(org), costElementArray, year, month);
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
					return true;
				}
				
				@Override
				protected int getYear() {
					return year;
				}

				@Override
				protected int getMonth() {
					return month;
				}

				@Override
				protected int getDay() {
					return day;
				}

				@Override
				protected BasicDBObject getProjectETLQuery() {
					BasicDBObject query = new BasicDBObject();
					query.put(
							Project.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
									Project.STATUS_CANCELED_VALUE,
									Project.STATUS_FINIHED_VALUE,
									Project.STATUS_PAUSED_VALUE,
									Project.STATUS_WIP_VALUE }));
					query.put(
							"$or", //$NON-NLS-1$
							new BasicDBObject[] {
									new BasicDBObject().append(
											Project.F_PLAN_START,
											new BasicDBObject().append("$lte", //$NON-NLS-1$
													cal.getTime())),

									new BasicDBObject().append(
											Project.F_ACTUAL_START,
											new BasicDBObject().append("$lte", //$NON-NLS-1$
													cal.getTime())) });
					query.put(Project.F_FUNCTION_ORGANIZATION, org_id);

					return query;
				}
			};
			etl.run();
		}

		return true;
	}
}
