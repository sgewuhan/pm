package com.sg.business.model.etl;

import java.util.Calendar;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mongodb.BasicDBObject;
import com.sg.business.model.Project;

public class ETLOldJob implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		for (int i = 24; i >= 0; i--) {
			final Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.DATE, -1);

			final int year = cal.get(Calendar.YEAR);
			final int month = cal.get(Calendar.MONTH) + 1;
			final int day = 1;
			System.out.println("" + year + "-" + month + "-" + day);
			AbstractETLJob etl = new AbstractETLJob() {

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
							new BasicDBObject().append("$in", new String[] {
									Project.STATUS_CANCELED_VALUE,
									Project.STATUS_FINIHED_VALUE,
									Project.STATUS_PAUSED_VALUE,
									Project.STATUS_WIP_VALUE }));
					query.put(
							"$or",
							new BasicDBObject[] {
									new BasicDBObject().append(
											Project.F_PLAN_START,
											new BasicDBObject().append("$lte",
													cal.getTime())),

									new BasicDBObject().append(
											Project.F_ACTUAL_START,
											new BasicDBObject().append("$lte",
													cal.getTime())) });

					return query;
				}
			};
			etl.run();
		}

		return true;
	}
}
