package com.sg.sales.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.DBActivator;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.sales.Sales;

public class PerformenceUtil {

	private static Object[] getSalesUserCondition(String userId) {
		return new Object[] {
				new BasicDBObject().append(ISalesTeam.F_SALES_MANAGER, userId),
				new BasicDBObject().append(ISalesTeam.F_SALES_SUP, userId),
				new BasicDBObject().append(ISalesTeam.F_CUSTOMER_REP, userId),
				new BasicDBObject().append(TeamControl.F_OWNER, userId) };
	}

	/**
	 * 本月新增客户数量，
	 * 
	 * 查询创建日期在本月的公司数量 客户的类别为
	 * 
	 * "战略客户","普通客户","准客户","潜在客户"
	 * 
	 * 客户等级"A","B","C","D"
	 * 
	 * @param userId
	 * @param month
	 * @return
	 */
	public static long getCustomerQtyMonth(String userId, int month) {
		final DBObject query = new BasicDBObject();
		final Date[] date = Utils.getStartEndOfMonth(month);
		query.put(Company.F__CDATE, new BasicDBObject().append("$gte", date[0])
				.append("$lte", date[1]));
		query.put(
				Company.F_SERVICELEVEL,
				new BasicDBObject().append("$in", new String[] { "战略客户",
						"普通客户", "准客户", "潜在客户" }));
		query.put(
				Company.F_LEVEL,
				new BasicDBObject().append("$in", new String[] { "A", "B", "C",
						"D" }));
		query.put("$or", getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_COMPANY);
		return col.count(query);
	}

	/**
	 * 客户保有数量 公司数量 客户的类别
	 * 
	 * 为"战略客户","普通客户","准客户","潜在客户" 客户等级"A","B","C","D"
	 * 
	 * @param userId
	 * @param month
	 * @return
	 */
	public static long getCustomerQty(String userId) {
		final DBObject query = new BasicDBObject();
		query.put(
				Company.F_SERVICELEVEL,
				new BasicDBObject().append("$in", new String[] { "战略客户",
						"普通客户", "准客户", "潜在客户" }));
		query.put(
				Company.F_LEVEL,
				new BasicDBObject().append("$in", new String[] { "A", "B", "C",
						"D" }));
		query.put("$or", getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_COMPANY);
		return col.count(query);
	}

	/**
	 * 本月新增销售费用
	 */
	public static double getSalesCostAmountMonth(String userId, int month) {
		BasicDBObject query = new BasicDBObject();
		final Date[] date = Utils.getStartEndOfMonth(month);
		query.put(
				"$match",
				new BasicDBObject().append(
						WorkCost.F_FINISHDATE,
						new BasicDBObject().append("$gte", date[0]).append(
								"$lte", date[1])).append(WorkCost.F_OWNER,
						userId));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + WorkCost.F_OWNER)
						.append(WorkCost.F_OWNER,
								new BasicDBObject().append("$sum", "$"
										+ WorkCost.F_ACTUAL_AMOUNT)));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_WORKCOST);
		AggregationOutput output = col.aggregate(query, group);
		Iterator<DBObject> iter = output.results().iterator();
		if (iter.hasNext()) {
			DBObject result = iter.next();
			Object value = result.get(WorkCost.F_OWNER);
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			}
		}
		return 0;
	}

	/**
	 * 全年累计销售费用
	 */
	public static double getSalesCostAmountSum(String userId) {
		BasicDBObject query = new BasicDBObject();
		query.put("$match",
				new BasicDBObject().append(WorkCost.F_OWNER, userId));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", "$" + WorkCost.F_OWNER)
						.append(WorkCost.F_OWNER,
								new BasicDBObject().append("$sum", "$"
										+ WorkCost.F_ACTUAL_AMOUNT)));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_WORKCOST);
		AggregationOutput output = col.aggregate(query, group);
		Iterator<DBObject> iter = output.results().iterator();
		if (iter.hasNext()) {
			DBObject result = iter.next();
			Object value = result.get(WorkCost.F_OWNER);
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			}
		}
		return 0;
	}

	/**
	 * 有效电话活动数量（本月）
	 */
	public static long getFinishedCallMonth(String userId, int month) {
		// 查询work中，type为电话的，有效的
		final Date[] date = Utils.getStartEndOfMonth(month);
		BasicDBObject query = new BasicDBObject();
		query.put(Work.F_CHARGER, userId);
		query.put(ISalesWork.F_ACTIONTYPE, "电话联系");
		query.put(Work.F_ACHIEVED, Boolean.TRUE);
		query.put(
				Work.F_ACTUAL_FINISH,
				new BasicDBObject().append("$gte", date[0]).append("$lte",
						date[1]));
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		return col.count(query);
	}

	/**
	 * 有效拜访活动数量（本月）
	 */
	public static long getFinishedVisitMonth(String userId, int month) {
		final Date[] date = Utils.getStartEndOfMonth(month);
		BasicDBObject query = new BasicDBObject();
		query.put(Work.F_CHARGER, userId);
		query.put(
				ISalesWork.F_ACTIONTYPE,
				new BasicDBObject().append("$in", new String[] { "公司拜访",
						"售前技术", "商务活动" }));
		query.put(Work.F_ACHIEVED, Boolean.TRUE);
		query.put(
				Work.F_ACTUAL_FINISH,
				new BasicDBObject().append("$gte", date[0]).append("$lte",
						date[1]));
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);
		return col.count(query);
	}

	/**
	 * 该月新增销售线索
	 * 
	 * @param userId
	 * @param month
	 * @return
	 */
	public static long getLeadsQtyMonth(String userId, int month) {
		final DBObject query = new BasicDBObject();
		final Date[] date = Utils.getStartEndOfMonth(month);
		query.put(
				Opportunity.F__CDATE,
				new BasicDBObject().append("$gte", date[0]).append("$lte",
						date[1]));
		query.put(Opportunity.F_PROGRESS, "识别需求");
		query.put("$or", getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_OPPORTUNITY);
		return col.count(query);
	}

	public static long getLeadsQtySum(String userId) {
		final DBObject query = new BasicDBObject();
		query.put(Opportunity.F_PROGRESS, "识别需求");
		query.put("$or", getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_OPPORTUNITY);
		return col.count(query);
	}

	public static long getOpportunityQtyMonth(String userId, int month) {
		final Date[] date = Utils.getStartEndOfMonth(month);
		final DBObject query = new BasicDBObject();
		query.put(
				Opportunity.F__CDATE,
				new BasicDBObject().append("$gte", date[0]).append("$lte",
						date[1]));
		query.put(
				Opportunity.F_PROGRESS,
				new BasicDBObject().append("$in", new String[] { "预算规划",
						"交流选型", "招标谈判" }));
		query.put("$or", getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_OPPORTUNITY);
		return col.count(query);
	}

	public static long getOpportunityQtySum(String userId) {
		final DBObject query = new BasicDBObject();
		query.put(
				Opportunity.F_PROGRESS,
				new BasicDBObject().append("$in", new String[] { "预算规划",
						"交流选型", "招标谈判" }));
		query.put("$or", getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_OPPORTUNITY);
		return col.count(query);
	}

	public static long getContractQtySeason(String userId, int month) {
		final Date[] date = Utils.getStartEndOfSeason(month);
		return getContractQtyByDate(userId, date);
	}

	public static long getContractQtyYear(String userId, int year) {
		final Date[] date = Utils.getStartEndOfYear(year);
		return getContractQtyByDate(userId, date);
	}

	private static long getContractQtyByDate(String userId, final Date[] date) {
		final DBObject query = new BasicDBObject();
		query.put(
				Contract.F_EFFECTIVEON,
				new BasicDBObject().append("$gte", date[0]).append("$lte",
						date[1]));
		query.put("$or",getSalesUserCondition(userId));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_CONTACT);
		return col.count(query);
	}

	/**
	 * 本季度合同额
	 * 
	 * @param userId
	 * @param month
	 * @return
	 */
	public static double getContractAmountSeason(String userId, int month) {
		final Date[] date = Utils.getStartEndOfSeason(month);
		return getContractAmountByDate(userId, date);
	}

	public static double getContractAmountYear(String userId, int year) {
		final Date[] date = Utils.getStartEndOfYear(year);
		return getContractAmountByDate(userId, date);
	}

	private static double getContractAmountByDate(String userId,
			final Date[] date) {
		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(
						Contract.F_EFFECTIVEON,
						new BasicDBObject().append("$gte", date[0]).append(
								"$lte", date[1]))
								.append("$or",getSalesUserCondition(userId)));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", new ObjectId())
						.append(Contract.F_OWNER,
								new BasicDBObject().append("$sum", "$"
										+ Contract.F_AMOUNT)));

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_CONTRACT);
		AggregationOutput output = col.aggregate(query, group);
		Iterator<DBObject> iter = output.results().iterator();
		if (iter.hasNext()) {
			DBObject result = iter.next();
			Object value = result.get(Contract.F_OWNER);
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			}
		}
		return 0;
	}

	private static double getIncomeAmountSeasonByDate(String userId,
			final Date[] date) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				Sales.C_CONTRACT);
		DBCursor cur = col.find(
				new BasicDBObject().append("$or",getSalesUserCondition(userId)),
				new BasicDBObject().append(Contract.F__ID, 1));
		ArrayList<ObjectId> contractIdList = new ArrayList<ObjectId>();
		while (cur.hasNext()) {
			contractIdList.add((ObjectId) cur.next().get(Contract.F__ID));
		}
		if (contractIdList.isEmpty()) {
			return 0d;
		}

		BasicDBObject query = new BasicDBObject();
		query.put(
				"$match",
				new BasicDBObject().append(
						Income.F_TRANSFERDATE,
						new BasicDBObject().append("$gte", date[0]).append(
								"$lte", date[1])).append(Income.F_CONTRACT_ID,
						new BasicDBObject().append("$in", contractIdList)));

		BasicDBObject group = new BasicDBObject();
		group.put(
				"$group",
				new BasicDBObject().append("_id", new ObjectId()).append(
						Income.F_AMOUNT,
						new BasicDBObject().append("$sum", "$"
								+ Income.F_AMOUNT)));

		col = DBActivator.getCollection(IModelConstants.DB, Sales.C_INCOME);
		AggregationOutput output = col.aggregate(query, group);
		Iterator<DBObject> iter = output.results().iterator();
		if (iter.hasNext()) {
			DBObject result = iter.next();
			Object value = result.get(Income.F_AMOUNT);
			if (value instanceof Number) {
				return ((Number) value).doubleValue();
			}
		}
		return 0;
	}

	public static double getIncomeAmountSeason(String userId, int month) {
		final Date[] date = Utils.getStartEndOfMonth(month);
		return getIncomeAmountSeasonByDate(userId, date);
	}

	public static double getIncomeAmountYear(String userId, int year) {
		final Date[] date = Utils.getStartEndOfYear(year);
		return getIncomeAmountSeasonByDate(userId, date);
	}

}
