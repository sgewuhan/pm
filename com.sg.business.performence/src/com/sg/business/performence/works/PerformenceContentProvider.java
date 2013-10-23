package com.sg.business.performence.works;

import java.util.ArrayList;
import java.util.Calendar;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;
import com.sg.business.model.WorksPerformence;
import com.sg.widgets.registry.config.TreeConfigurator;
import com.sg.widgets.viewer.RelationContentProvider;

public class PerformenceContentProvider extends RelationContentProvider {

	private DBCollection collection;

	public PerformenceContentProvider() {
		super();
		init();
	}

	public PerformenceContentProvider(TreeConfigurator configurator) {
		super(configurator);
		init();
	}

	private void init() {
		collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORKS_PERFORMENCE);
	}

	@Override
	public boolean hasChildren(Object parentElement) {
		return !(parentElement instanceof WorksPerformence);
	}
	
	@Override
	public Object[] getChildren(Object parent) {
		if (parent instanceof User) {
			// 取出最早的日期
			DBCursor cur = collection
					.find(new BasicDBObject().append(WorksPerformence.F_USERID,
							((User) parent).getUserid()),
							new BasicDBObject().append(
									WorksPerformence.F_USERID, 1).append(
											WorksPerformence.F_DATECODE, 1))
					.sort(new BasicDBObject().append(
							WorksPerformence.F_DATECODE, 1)).limit(1);
			if (cur.hasNext()) {
				DBObject data = cur.next();
				Object dateCode = data.get(WorksPerformence.F_DATECODE);
				if (dateCode instanceof Long) {
					long time = ((Long) dateCode).longValue() * 24 * 60 * 60
							* 1000;
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(time);
					int yearCode = cal.get(Calendar.YEAR);
					int monthCode = cal.get(Calendar.MONTH);
					Calendar today = Calendar.getInstance();
					int thisYearCode = today.get(Calendar.YEAR);
					int thisMonthCode = today.get(Calendar.MONTH);
					ArrayList<MonthFolder> result = new ArrayList<MonthFolder>();
					while (thisYearCode >= yearCode
							&& thisMonthCode >= monthCode) {
						DBObject dbo = new BasicDBObject()
								.append(MonthFolder.F_YEAR, thisYearCode)
								.append(MonthFolder.F_MONTH, thisMonthCode)
								.append(MonthFolder.F_USERID,
										((User) parent).getUserid());
						MonthFolder monthFolder = ModelService
								.createModelObject(dbo, MonthFolder.class);
						result.add(monthFolder);

						today.add(Calendar.MONTH, -1);
						thisYearCode = today.get(Calendar.YEAR);
						thisMonthCode = today.get(Calendar.MONTH);
					}
					return result.toArray();
				}
			}
			return new Object[0];
		} else if (parent instanceof MonthFolder) {
			MonthFolder folder = (MonthFolder) parent;
			String userid = (String) folder.getValue(MonthFolder.F_USERID);
			Integer year = (Integer) folder.getValue(MonthFolder.F_YEAR);
			Integer month = (Integer) folder.getValue(MonthFolder.F_MONTH);
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, 1, 0, 0, 0);
			long startCode = cal.getTimeInMillis() / (24 * 60 * 60 * 1000);
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.SECOND, -1);
			long endCode = cal.getTimeInMillis() / (24 * 60 * 60 * 1000);
			DBCursor cur = collection.find(new BasicDBObject().append(
					WorksPerformence.F_USERID, userid)
					.append("$and",
							new DBObject[] {
									new BasicDBObject().append(
											WorksPerformence.F_DATECODE,
											new BasicDBObject().append("$gt",
													startCode)),
									new BasicDBObject().append(
											WorksPerformence.F_DATECODE,
											new BasicDBObject().append("$lt",
													endCode)) }));

			Object[] result = new Object[cur.size()];
			int i = 0;
			while (cur.hasNext()) {
				result[i++] = ModelService.createModelObject(cur.next(),
						WorksPerformence.class);
			}

			return result;
		} else {
			return super.getChildren(parent);
		}
	}

}
