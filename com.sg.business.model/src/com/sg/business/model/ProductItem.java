package com.sg.business.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class ProductItem extends PrimaryObject implements IProjectRelative {

	/**
	 * 是否已转批
	 */

	public static final String F_IS_MASS_PRODUCTION = "status_massproduction"; //$NON-NLS-1$

	/**
	 * 销售收入
	 */
	public static final String F_SALES_INCOME = "sales_income"; //$NON-NLS-1$

	/**
	 * 销售成本
	 */
	public static final String F_SALES_COST = "sales_cost"; //$NON-NLS-1$

	/**
	 * 销售数据更新时间
	 */
	//	public static final String F_SALES_DATA_UPDATE = "sales_data_update"; //$NON-NLS-1$

	@Override
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	public boolean isInMassProduction() {
		return Boolean.TRUE.equals(getValue(F_IS_MASS_PRODUCTION));
	}

	public boolean canChangeToMassProduction() {
		return !Boolean.TRUE.equals(getValue(F_IS_MASS_PRODUCTION));
	}

	public void doChangeToMassProduction(IContext context) throws Exception {
		if (isInMassProduction()) {
			return;
		}
		setValue(F_IS_MASS_PRODUCTION, Boolean.TRUE);
		DBCollection col = getCollection();
		WriteResult ws = col.update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append("$set", new BasicDBObject().append( //$NON-NLS-1$
						F_IS_MASS_PRODUCTION, Boolean.TRUE)));
		checkWriteResult(ws);
	}

	public ObjectId getProject_id() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	@Override
	public boolean canEdit(IContext context) {
		return canChangeToMassProduction();
	}

	public double getSalesRevenue() {
		// 修改成从Map-reduce中取数
		DBCollection col = getCollection(IModelConstants.C_PRODUCT_SALESDATA);
		BasicDBObject query = new BasicDBObject();
		query.put(F__ID, getDesc());
		BasicDBObject fields = new BasicDBObject();
		fields.put("value." + F_SALES_INCOME, 1);
		DBObject object = col.findOne(query, fields);
		if (object != null) {
			DBObject value = (DBObject) object.get("value");
			Double sales_income = (Double) value.get(F_SALES_INCOME);
			return sales_income == null ? 0d : sales_income.doubleValue();
		} else {
			return 0;
		}
	}

	public double getSalesCost() {
		// 修改成从Map-reduce中取数
		DBCollection col = getCollection(IModelConstants.C_PRODUCT_SALESDATA);
		BasicDBObject query = new BasicDBObject();
		query.put(F__ID, getDesc());
		BasicDBObject fields = new BasicDBObject();
		fields.put("value." + F_SALES_COST, 1);
		DBObject object = col.findOne(query, fields);
		if (object != null) {
			DBObject value = (DBObject) object.get("value");
			Double sales_cost = (Double) value.get(F_SALES_COST);
			return sales_cost == null ? 0d : sales_cost.doubleValue();
		} else {
			return 0;
		}
	}

	public double[] getMonthlySalesData(Calendar cal) {
		double[] result = new double[] { 0d, 0d };

		DBCollection col = getCollection(IModelConstants.C_SALESDATA);
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(SalesData.F_MATERIAL_NUMBER, getDesc());
		matchCondition.put(SalesData.F_ACCOUNT_YEAR,
				"" + cal.get(Calendar.YEAR));
		matchCondition.put(SalesData.F_ACCOUNT_MONTH,
				String.format("%03d", (cal.get(Calendar.MONTH) + 1)));
		DBObject match = new BasicDBObject().append("$match", matchCondition);
		DBObject groupCondition = new BasicDBObject();
		groupCondition.put("_id", "$" + SalesData.F_MATERIAL_NUMBER);// 按物料分组
		groupCondition.put(SalesData.F_SALES_COST, new BasicDBObject().append(
				"$sum", "$" + SalesData.F_SALES_COST));// 销售成本
		groupCondition.put(
				SalesData.F_SALES_INCOME,
				new BasicDBObject().append("$sum", "$"
						+ SalesData.F_SALES_INCOME));// 销售收入
		groupCondition.put(
				SalesData.F_COST_VAR_APP,
				new BasicDBObject().append("$sum", "$"
						+ SalesData.F_COST_VAR_APP));// 销售成本差异

		DBObject group = new BasicDBObject().append("$group", groupCondition);
		AggregationOutput agg = col.aggregate(match, group);
		Iterator<DBObject> iter = agg.results().iterator();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			Number value = (Number) data.get(SalesData.F_SALES_INCOME);
			result[0] += value.doubleValue();
			value = (Number) data.get(SalesData.F_SALES_COST);
			result[1] += value.doubleValue();
			value = (Number) data.get(SalesData.F_COST_VAR_APP);
			result[1] += value.doubleValue();
		}
		return result;
	}

	public double getSalesRevenueByCalendar(Calendar cal) {
		double result = 0;

		DBCollection col = getCollection(IModelConstants.C_SALESDATA);
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(SalesData.F_MATERIAL_NUMBER, getDesc());

		List<BasicDBObject> matchDateList = new ArrayList<BasicDBObject>();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		for (int i = 2009; i <= year; i++) {
			for (int j = 0; j < 12; j++) {
				if (i == year && j >= month) {
					continue;
				}
				BasicDBObject dbo = new BasicDBObject();
				dbo.put(WorkOrderPeriodCost.F_YEAR, i);
				dbo.put(WorkOrderPeriodCost.F_MONTH, j + 1);
				matchDateList.add(dbo);
			}
		}
		BasicDBObject[] matchDate = matchDateList.toArray(new BasicDBObject[0]);
		if (matchDate.length > 0) {
			matchCondition.put("$or", matchDate);
		}

		DBObject match = new BasicDBObject().append("$match", matchCondition);
		DBObject groupCondition = new BasicDBObject();
		groupCondition.put("_id", "$" + SalesData.F_MATERIAL_NUMBER);// 按物料分组
		groupCondition.put(
				SalesData.F_SALES_INCOME,
				new BasicDBObject().append("$sum", "$"
						+ SalesData.F_SALES_INCOME));// 销售收入

		DBObject group = new BasicDBObject().append("$group", groupCondition);
		AggregationOutput agg = col.aggregate(match, group);
		Iterator<DBObject> iter = agg.results().iterator();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			Number value = (Number) data.get(SalesData.F_SALES_INCOME);
			result += value.doubleValue();
		}
		return result;
	}

	public double getSalesCostByCalendar(Calendar cal) {
		double result = 0;

		DBCollection col = getCollection(IModelConstants.C_SALESDATA);
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(SalesData.F_MATERIAL_NUMBER, getDesc());

		List<BasicDBObject> matchDateList = new ArrayList<BasicDBObject>();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		for (int i = 2009; i <= year; i++) {
			for (int j = 0; j < 12; j++) {
				if (i == year && j >= month) {
					continue;
				}
				BasicDBObject dbo = new BasicDBObject();
				dbo.put(WorkOrderPeriodCost.F_YEAR, i);
				dbo.put(WorkOrderPeriodCost.F_MONTH, j + 1);
				matchDateList.add(dbo);
			}
		}
		BasicDBObject[] matchDate = matchDateList.toArray(new BasicDBObject[0]);
		if (matchDate.length > 0) {
			matchCondition.put("$or", matchDate);
		}

		DBObject match = new BasicDBObject().append("$match", matchCondition);
		DBObject groupCondition = new BasicDBObject();
		groupCondition.put("_id", "$" + SalesData.F_MATERIAL_NUMBER);// 按物料分组
		groupCondition.put(SalesData.F_SALES_COST, new BasicDBObject().append(
				"$sum", "$" + SalesData.F_SALES_COST));// 销售成本
		groupCondition.put(
				SalesData.F_COST_VAR_APP,
				new BasicDBObject().append("$sum", "$"
						+ SalesData.F_COST_VAR_APP));// 销售成本差异

		DBObject group = new BasicDBObject().append("$group", groupCondition);
		AggregationOutput agg = col.aggregate(match, group);
		Iterator<DBObject> iter = agg.results().iterator();
		while (iter.hasNext()) {
			DBObject data = iter.next();
			Number value = (Number) data.get(SalesData.F_SALES_COST);
			result += value.doubleValue();
			value = (Number) data.get(SalesData.F_COST_VAR_APP);
			result += value.doubleValue();
		}
		return result;
	}
}
