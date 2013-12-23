package com.sg.business.model;

import java.util.Calendar;
import java.util.Iterator;

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
	public static final String F_IS_MASS_PRODUCTION = "status_massproduction";

	/**
	 * 销售收入
	 */
	public static final String F_SALES_INCOME = "sales_income";

	/**
	 * 销售成本
	 */
	public static final String F_SALES_COST = "sales_cost";

	/**
	 * 销售数据更新时间
	 */
	public static final String F_SALES_DATA_UPDATE = "sales_data_update";

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
				new BasicDBObject().append("$set", new BasicDBObject().append(
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

	/**
	 * 计算销售收入
	 * 
	 * @param dateCode
	 */
	public void doCalculateSalesData(String dateCode) {
		double[] result = new double[] { 0d, 0d };

		DBCollection col = getCollection(IModelConstants.C_SALESDATA);
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(SalesData.F_MATERIAL_NUMBER, getDesc());
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

		getCollection().update(
				new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append(
						"$set",
						new BasicDBObject().append(F_SALES_INCOME, result[0])
								.append(F_SALES_COST, result[1])
								.append(F_SALES_DATA_UPDATE, dateCode)));
		// System.out.println("更新销售数据:"+getDesc());
	}

	public double getSalesRevenue() {
		Double value = getDoubleValue(F_SALES_INCOME);
		return value == null ? 0d : value.doubleValue();
	}

	public double getSalesCost() {
		Double value = getDoubleValue(F_SALES_COST);
		return value == null ? 0d : value.doubleValue();
	}

	public double[] getMonthlySalesData(Calendar cal) {
		double[] result = new double[] { 0d, 0d };

		DBCollection col = getCollection(IModelConstants.C_SALESDATA);
		DBObject matchCondition = new BasicDBObject();
		matchCondition.put(SalesData.F_MATERIAL_NUMBER, getDesc());
		matchCondition.put(SalesData.F_ACCOUNT_YEAR, ""+cal.get(Calendar.YEAR));
		matchCondition.put(SalesData.F_ACCOUNT_MONTH,
				String.format("%03d",(cal.get(Calendar.MONTH) + 1)));
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
}
