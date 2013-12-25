package com.sg.business.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.Util;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.model.nls.Messages;

public abstract class ProjectProvider extends PrimaryObject {

	public ProjectSetSummaryData sum;

	/**
	 * 参数名称：按年计算
	 */
	public static final String PARAMETER_SUMMARY_BY_YEAR = "y"; //$NON-NLS-1$

	/**
	 * 参数名称:按季度计算
	 */
	public static final String PARAMETER_SUMMARY_BY_QUARTER = "q"; //$NON-NLS-1$

	/**
	 * 参数:按月计算
	 */
	public static final String PARAMETER_SUMMARY_BY_MONTH = "m"; //$NON-NLS-1$

	public Object[] parameters;

	private ListenerList listeners;

	private boolean isDirty = true;

	private List<PrimaryObject> projectSetData;

	public ProjectProvider() {
		super();
		sum = new ProjectSetSummaryData();
		parameters = new Object[2];
		parameters[0] = Calendar.getInstance();
		parameters[1] = ProjectProvider.PARAMETER_SUMMARY_BY_YEAR;
	}

	public abstract List<PrimaryObject> getProjectSet();

	/**
	 * 获得项目集合的名称
	 * 
	 * @return
	 */
	public abstract String getProjectSetName();

	/**
	 * 获得项目集封面图片
	 * 
	 * @return
	 */
	public abstract String getProjectSetCoverImage();

	public abstract List<ObjectId> getAllProjectId();

	/**
	 * 设置查询参数
	 * 
	 * @param parameters
	 */
	public void setParameters(Object[] parameters) {
		if (!Util.equals(this.parameters, parameters)) {
			Object[] oldParameters = this.parameters;

			if (parameters != null) {
				this.parameters = new Object[parameters.length];
				for (int i = 0; i < parameters.length; i++) {
					this.parameters[i] = parameters[i];
				}
			} else {
				this.parameters = parameters;
			}

			parameterChanged(oldParameters, parameters);
			isDirty = true;
		}
	}

	/**
	 * 获取查询参数
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return this.parameters;
	}

	final protected Date getStartDate() throws Exception {
		if (parameters == null) {
			return null;
		} else {
			Date start;
			Calendar calendar = (Calendar) parameters[0];
			switch ((String) parameters[1]) {
			case PARAMETER_SUMMARY_BY_YEAR:
				calendar.set(Calendar.MONTH, 0);
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				start = calendar.getTime();
				return start;
			case PARAMETER_SUMMARY_BY_QUARTER:
				int i = calendar.get(Calendar.MONTH);
				calendar.set(Calendar.MONTH, i / 3 * 3);
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				start = calendar.getTime();
				return start;
			case PARAMETER_SUMMARY_BY_MONTH:
				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				start = calendar.getTime();
				return start;
			default:
				throw new Exception(Messages.get().ProjectProvider_0);
			}
		}

	}

	final protected Date getEndDate() throws Exception {
		if (parameters == null) {
			return null;
		} else {
			Date end;
			Calendar calendar = Calendar.getInstance();
			Date start = getStartDate();
			if (start == null) {
				return null;
			} else {
				calendar.setTime(start);
				switch ((String) parameters[1]) {
				case PARAMETER_SUMMARY_BY_YEAR:
					calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
					calendar.set(Calendar.MILLISECOND,
							calendar.get(Calendar.MILLISECOND) - 1);
					end = calendar.getTime();
					return end;
				case PARAMETER_SUMMARY_BY_QUARTER:
					calendar.set(Calendar.MONTH,
							calendar.get(Calendar.MONTH) + 3);
					calendar.set(Calendar.MILLISECOND,
							calendar.get(Calendar.MILLISECOND) - 1);
					end = calendar.getTime();
					return end;
				case PARAMETER_SUMMARY_BY_MONTH:
					calendar.set(Calendar.MONTH,
							calendar.get(Calendar.MONTH) + 1);
					calendar.set(Calendar.MILLISECOND,
							calendar.get(Calendar.MILLISECOND) - 1);
					end = calendar.getTime();
					return end;
				default:
					throw new Exception(Messages.get().ProjectProvider_1);
				}
			}
		}
	}

	final protected Date getLastStartDate() throws Exception {
		if (parameters == null) {
			return null;
		} else {
			Date start = getStartDate();
			Calendar calendar = (Calendar) parameters[0];
			calendar.setTime(start);
			switch ((String) parameters[1]) {
			case PARAMETER_SUMMARY_BY_YEAR:
				calendar.add(Calendar.YEAR, -1);
				start = calendar.getTime();
				return start;
			case PARAMETER_SUMMARY_BY_QUARTER:
				calendar.add(Calendar.MONTH, -3);
				start = calendar.getTime();
				return start;
			case PARAMETER_SUMMARY_BY_MONTH:
				calendar.add(Calendar.MONTH, -1);
				start = calendar.getTime();
				return start;
			default:
				throw new Exception("时间参数异常");
			}
		}
	}

	final protected Date getLastEndDate() throws Exception {
		if (parameters == null) {
			return null;
		} else {
			Date end = getEndDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(end);
			switch ((String) parameters[1]) {
			case PARAMETER_SUMMARY_BY_YEAR:
				calendar.add(Calendar.YEAR, -1);
				end = calendar.getTime();
				return end;
			case PARAMETER_SUMMARY_BY_QUARTER:
				calendar.add(Calendar.MONTH, -3);
				end = calendar.getTime();
				return end;
			case PARAMETER_SUMMARY_BY_MONTH:
				calendar.add(Calendar.MONTH, -1);
				end = calendar.getTime();
				return end;
			default:
				throw new Exception("时间参数异常");
			}
		}
	}

	public void addParameterChangedListener(IParameterListener listener) {
		if (listeners == null) {
			listeners = new ListenerList();
		}
		listeners.add(listener);
	}

	public void removeParameterChangedListener(IParameterListener listener) {
		if (listeners != null && listener != null) {
			listeners.remove(listener);
		}
	}

	private void parameterChanged(Object[] oldParameters, Object[] newParameters) {
		if (listeners != null && listeners.size() > 0) {
			Object[] lis = listeners.getListeners();
			for (int i = 0; i < lis.length; i++) {
				((IParameterListener) lis[i]).parameterChanged(oldParameters,
						newParameters);
			}
		}
	}

	public List<PrimaryObject> getData() {
		if (isDirty) {
			projectSetData = getProjectSet();
			isDirty = false;
		}
		return projectSetData;
	}

	protected BasicDBObject getQueryCondtion(Date start, Date stop) {
		BasicDBObject dbo = new BasicDBObject();
		dbo.put(ILifecycle.F_LIFECYCLE,
				new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
						ILifecycle.STATUS_FINIHED_VALUE,
								ILifecycle.STATUS_WIP_VALUE }));
		if (start != null && stop != null) {
			dbo.put(Project.F_ACTUAL_START,
					new BasicDBObject().append("$gte", start).append("$lte",
							stop));

		}

		return dbo;
	}

	public double[] getRateValueByYear(String field) {
		AggregationOutput aggregationOutput = aggregateCountByYear(field);
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		double[] delay = new double[12];
		double[] nodelay = new double[12];
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			DBObject _id = (DBObject) dbObject.get("_id");
			int month = (int) _id.get("month");
			boolean isdelay = (boolean) _id.get(field);
			if (isdelay) {
				delay[month - 1] = ((Number) dbObject.get("number"))
						.doubleValue();
			} else {
				nodelay[month - 1] = ((Number) dbObject.get("number"))
						.doubleValue();
			}
		}
		double[] rate = new double[12];
		for (int i = 0; i < delay.length; i++) {
			if (delay[i] != 0d) {
				BigDecimal d = new BigDecimal(100d * delay[i]
						/ (delay[i] + nodelay[i]));
				rate[i] = d.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			} else {
				rate[i] = 0d;
			}
		}
		return rate;
	}

	private AggregationOutput aggregateCountByYear(String field) {
		Calendar cal = parameters == null ? Calendar.getInstance()
				: (Calendar) parameters[0];
		int year = cal.get(Calendar.YEAR);
		List<ObjectId> projectIdList = getAllProjectId();
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year).append(
						ProjectETL.F_PROJECTID,
						new BasicDBObject().append("$in", projectIdList)));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append(
						"_id",
						new BasicDBObject().append("month", "$month").append(
								field, "$" + field)).append("number",
						new BasicDBObject().append("$sum", 1)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append("_id", 1));

		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort);
		return aggregationOutput;
	}

	public double[] getProfitRateByYear() {
		AggregationOutput aggregationOutput = aggregateRevenueAndCostByYear();
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		double[] sales_cost = new double[12];
		double[] sales_revenue = new double[12];
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			int month = (int) dbObject.get("_id");
			sales_cost[month - 1] = ((Number) dbObject
					.get(ProjectETL.F_SALES_COST)).doubleValue();
			sales_revenue[month - 1] = ((Number) dbObject
					.get(ProjectETL.F_SALES_REVENUE)).doubleValue();
		}
		double[] rate = new double[12];
		for (int i = 0; i < sales_revenue.length; i++) {
			if (sales_revenue[i] != 0d) {
				BigDecimal d = new BigDecimal(100d
						* (sales_revenue[i] - sales_cost[i]) / sales_revenue[i]);
				rate[i] = d.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			} else {
				rate[i] = 0d;
			}
		}
		return rate;
	}

	private AggregationOutput aggregateRevenueAndCostByYear() {
		Calendar cal = parameters == null ? Calendar.getInstance()
				: (Calendar) parameters[0];
		int year = cal.get(Calendar.YEAR);
		List<ObjectId> projectIdList = getAllProjectId();
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year).append(
						ProjectETL.F_PROJECTID,
						new BasicDBObject().append("$in", projectIdList)));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject()
						.append("_id", "$month")
						.append(ProjectETL.F_SALES_COST,
								new BasicDBObject().append("$sum", "$"
										+ ProjectETL.F_SALES_COST))
						.append(ProjectETL.F_SALES_REVENUE,
								new BasicDBObject().append("$sum", "$"
										+ ProjectETL.F_SALES_REVENUE)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append("_id", 1));
		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort);
		return aggregationOutput;
	}

	public double[] getValueByYear(String[] fields) {
		Calendar cal = parameters == null ? Calendar.getInstance()
				: (Calendar) parameters[0];
		int year = cal.get(Calendar.YEAR);
		List<ObjectId> projectIdList = getAllProjectId();
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year).append(
						ProjectETL.F_PROJECTID,
						new BasicDBObject().append("$in", projectIdList)));

		BasicDBObject groupvalue = new BasicDBObject();
		groupvalue.put("_id", "$month");
		for (String field : fields) {
			groupvalue.put(field,
					new BasicDBObject().append("$sum", "$" + field));
		}

		BasicDBObject group = new BasicDBObject();
		group.put("$group", groupvalue);

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append("_id", 1));

		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort);
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		double[] total = new double[] { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d,
				0d, 0d };
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			int month = (int) dbObject.get("_id");
			for (String field : fields) {
				double doubleValue = ((Number) dbObject.get(field))
						.doubleValue();
				BigDecimal d = new BigDecimal(doubleValue / 10000d);
				total[month - 1] = total[month - 1]
						+ d.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
			}

		}
		return total;
	}

	public double[][] getProfitAndCostByYear() {
		AggregationOutput aggregationOutput = aggregatProfitAndCostByYear();
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		double[] sales_cost = new double[12];
		double[] sales_profit = new double[12];
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			int month = (int) dbObject.get("_id");
			sales_cost[month - 1] = ((Number) dbObject
					.get(ProjectETL.F_MONTH_SALES_COST)).doubleValue();
			sales_profit[month - 1] = ((Number) dbObject
					.get(ProjectETL.F_MONTH_SALES_PROFIT)).doubleValue();
		}
		for (int i = 0; i < sales_cost.length; i++) {
			if (sales_profit[i] != 0d) {
				BigDecimal d = new BigDecimal((sales_profit[i]) / 10000d);
				sales_profit[i] = d.setScale(0, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
				d = new BigDecimal((sales_cost[i]) / 10000d);
				sales_cost[i] = d.setScale(0, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
			}
		}
		return new double[][] { sales_profit, sales_cost };
	}

	private AggregationOutput aggregatProfitAndCostByYear() {
		Calendar cal = parameters == null ? Calendar.getInstance()
				: (Calendar) parameters[0];
		int year = cal.get(Calendar.YEAR);
		List<ObjectId> projectIdList = getAllProjectId();
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(ProjectETL.F_YEAR, year).append(
						ProjectETL.F_PROJECTID,
						new BasicDBObject().append("$in", projectIdList)));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject()
						.append("_id", "$month")
						.append(ProjectETL.F_MONTH_SALES_COST,
								new BasicDBObject().append("$sum", "$"
										+ ProjectETL.F_MONTH_SALES_COST))
						.append(ProjectETL.F_MONTH_SALES_PROFIT,
								new BasicDBObject().append("$sum", "$"
										+ ProjectETL.F_MONTH_SALES_PROFIT)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append("_id", 1));
		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort);
		return aggregationOutput;
	}

	public double[][] getDelayValueByYear() {
		AggregationOutput aggregationOutput = aggregateCountByYear("isdelay_def");
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		double[] delay = new double[12];
		double[] nodelay = new double[12];
		while (iterator.hasNext()) {
			DBObject dbObject = (DBObject) iterator.next();
			DBObject _id = (DBObject) dbObject.get("_id");
			int month = (int) _id.get("month");
			boolean isdelay = (boolean) _id.get("isdelay_def");
			if (isdelay) {
				delay[month - 1] = ((Number) dbObject.get("number"))
						.doubleValue();
			} else {
				nodelay[month - 1] = ((Number) dbObject.get("number"))
						.doubleValue();
			}
		}
		return new double[][] { nodelay, delay };
	}

	public Object[] getHasLastNumberTopList(int limitNumber, int sortType,
			String groupField, String groupByField, int year, int month)
			throws Exception {
		List<ObjectId> projectIdList = getAllProjectId();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);

		Object[] topList = getTopList(limitNumber, sortType, groupField,
				groupByField, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, projectIdList);
		cal.add(Calendar.MONTH, -1);
		Object[] lastopList = getTopList(limitNumber, sortType, groupField,
				groupByField, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, projectIdList);

		int selectIndex = 0;
		int insertIndex = 3;
		int fromIndex = 2;
		compareArray(topList, lastopList, selectIndex, insertIndex, fromIndex);

		return topList;
	}

	public Object[] getHasLastNumberTopList(int limitNumber, int sortType,
			String groupField, String groupByField, String unWindField,
			int year, int month) throws Exception {
		List<ObjectId> projectIdList = getAllProjectId();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);

		Object[] topList = getTopList(limitNumber, sortType, groupField,
				groupByField, unWindField, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, projectIdList);
		cal.add(Calendar.MONTH, -1);
		Object[] lastopList = getTopList(limitNumber, sortType, groupField,
				groupByField, unWindField, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, projectIdList);

		int selectIndex = 0;
		int insertIndex = 3;
		int fromIndex = 2;
		compareArray(topList, lastopList, selectIndex, insertIndex, fromIndex);

		return topList;
	}

	private void compareArray(Object[] topList, Object[] lastopList,
			int selectIndex, int insertIndex, int fromIndex) {
		for (Object object : topList) {
			if (object != null) {
				Object[] top = (Object[]) object;
				Object _id = top[selectIndex];
				for (Object lastOject : lastopList) {
					if (lastOject != null) {
						Object[] lastTop = (Object[]) lastOject;
						Object last_id = lastTop[selectIndex];
						if (_id.equals(last_id)) {
							top[insertIndex] = lastTop[fromIndex];
						}
					}
				}
			}
		}
	}

	public Object[] getTopList(int limitNumber, int sortType,
			String groupField, String groupByField, int year, int month)
			throws Exception {
		List<ObjectId> projectIdList = getAllProjectId();
		return getTopList(limitNumber, sortType, groupField, groupByField,
				year, month, projectIdList);
	}

	private Object[] getTopList(int limitNumber, int sortType,
			String groupField, String groupByField, int year, int month,
			List<ObjectId> projectIdList) {
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject()
						.append(ProjectETL.F_YEAR, year)
						.append(ProjectETL.F_PROJECTID,
								new BasicDBObject()
										.append("$in", projectIdList))
						.append(ProjectETL.F_MONTH, month));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + groupByField).append(
						groupField,
						new BasicDBObject().append("$sum", "$" + groupField)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append(groupField, sortType));

		BasicDBObject limit = new BasicDBObject();

		limit.put("$limit", limitNumber);

		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, group, sort,
				limit);
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		Object[] results = new Object[limitNumber];
		for (int i = 0; i < results.length; i++) {
			DBObject dbObject = (DBObject) iterator.next();
			Object _id = dbObject.get("_id");
			Double month_sales_profit = (Double) dbObject.get(groupField);
			results[i] = new Object[] { _id, month_sales_profit, i + 1, null };
		}
		return results;
	}

	public Object[] getTopList(int limitNumber, int sortType,
			String groupField, String groupByField, String unWindField,
			int year, int month) throws Exception {
		List<ObjectId> projectIdList = getAllProjectId();
		return getTopList(limitNumber, sortType, groupField, groupByField,
				unWindField, year, month, projectIdList);
	}

	private Object[] getTopList(int limitNumber, int sortType,
			String groupField, String groupByField, String unWindField,
			int year, int month, List<ObjectId> projectIdList) {
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject()
						.append(ProjectETL.F_YEAR, year)
						.append(ProjectETL.F_PROJECTID,
								new BasicDBObject()
										.append("$in", projectIdList))
						.append(ProjectETL.F_MONTH, month));

		BasicDBObject unWind = new BasicDBObject();
		unWind.put("$unwind", "$" + unWindField);

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + groupByField).append(
						groupField,
						new BasicDBObject().append("$sum", "$" + groupField)));

		BasicDBObject sort = new BasicDBObject();
		sort.put("$sort", new BasicDBObject().append(groupField, sortType));

		BasicDBObject limit = new BasicDBObject();

		limit.put("$limit", limitNumber);

		DBCollection col = getCollection(IModelConstants.C_PROJECT_MONTH_DATA);
		AggregationOutput aggregationOutput = col.aggregate(query, unWind,
				group, sort, limit);
		Iterator<DBObject> iterator = aggregationOutput.results().iterator();
		Object[] results = new Object[limitNumber];
		for (int i = 0; i < results.length; i++) {
			if (iterator.hasNext()) {
				DBObject dbObject = (DBObject) iterator.next();
				Object _id = (Object) dbObject.get("_id");
				Double month_sales_profit = (Double) dbObject.get(groupField);
				results[i] = new Object[] { _id, month_sales_profit, i + 1,
						null };
			}
		}
		return results;
	}
}
