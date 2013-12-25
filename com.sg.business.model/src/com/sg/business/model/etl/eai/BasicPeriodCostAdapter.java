package com.sg.business.model.etl.eai;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkOrderPeriodCost;

public class BasicPeriodCostAdapter {

	public static final String ORGCODE = "org"; //$NON-NLS-1$
	public static final String COSECENTERCODE = "cost"; //$NON-NLS-1$
	public static final String ACCOUNTNUMERS = "account"; //$NON-NLS-1$
	public static final String YEAR = "year"; //$NON-NLS-1$
	public static final String MONTH = "month"; //$NON-NLS-1$


	/**
	 * 获得SAP成本中心期间研发成本的数据
	 * 
	 * @param orgCodeArray
	 *            , 组织代码数组，对应于成本中心代码数组
	 * @param costCodeArray
	 *            ，成本中心代码数组，对应于组织代码数组
	 * @param start
	 *            , 计算的期间开始时间
	 * @param end
	 *            , 期间结束时间
	 * @param account
	 *            , 研发成本科目, 为空时取全部科目
	 * @param costElementArray
	 * @param targetCollection
	 * @throws Exception
	 */
	public DBObject[] runGetData(String[] costCodeArray,
			String[] workordersArray, String[] costElementArray, int year,
			int month, String targetCollection)
			throws Exception {
		JCO_ZXFUN_PM_YFFY func = new JCO_ZXFUN_PM_YFFY();
		Map<String, Map<String, Double>> ret = func.getCost(costCodeArray,
				workordersArray, costElementArray, year, month);

		DBObject[] sr = new BasicDBObject[ret.size()];
		Iterator<String> iter = ret.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String costCenterCode = iter.next();
			sr[i] = new BasicDBObject();
			sr[i].put(WorkOrderPeriodCost.F_WORKORDER, costCenterCode);
			sr[i].put(WorkOrderPeriodCost.F_COSTCENTERCODE, costCenterCode);
			sr[i].put(WorkOrderPeriodCost.F_MONTH, new Integer(month));
			sr[i].put(WorkOrderPeriodCost.F_YEAR, new Integer(year));
			sr[i].put(WorkOrderPeriodCost.F__CDATE, new Date());
			Map<String, Double> values = ret.get(costCenterCode);
			if (values != null) {
				sr[i].putAll(values);
			}
			i++;
		}

		// 保存到数据库
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				targetCollection);
		col.insert(sr, WriteConcern.NORMAL);

		return sr;
	}

}
