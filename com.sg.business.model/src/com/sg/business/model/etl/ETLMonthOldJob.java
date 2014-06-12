package com.sg.business.model.etl;

import java.util.Calendar;
import java.util.List;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.Project;
import com.sg.business.model.toolkit.ProjectToolkit;

public class ETLMonthOldJob implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		final int day = 1;
		System.out.println("" + year + "-" + month + "-" + day); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		AbstractETLJob etl = new AbstractETLJob() {

			@Override
			protected boolean doETL(int year, int month, int day,
					String[] costElementArray, String[] workOrders,
					String[] costCodes) throws Exception {
				long start, end;
				
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
					doProjectMonthETL(year, month, projectETLList);
					end = System.currentTimeMillis();
					Commons.loginfo("[项目数据]更新项目月ETL数据完成:" + year + "-" + month + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							+ (end - start) / 1000 + " S");
				}
				return true;
			}
			
			@Override
			protected int getMonth() {
				cal.add(Calendar.DATE, -1);
				return cal.get(Calendar.MONTH) + 1;
			}

			@Override
			protected int getYear() {
				cal.add(Calendar.DATE, -1);
				return cal.get(Calendar.YEAR);
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
				return query;
			}
			@Override
			protected void clear(int year, int month) {
			}
		};
		etl.run();

		return true;
	}

}
