package com.sg.business.finance.eai;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.finance.eai.sap.JCO_ZXFUN_PM_YFFY;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.RNDPeriodCost;

public class BasicPeriodCostAdapter {

	public static final String ORGCODE = "org";
	public static final String COSECENTERCODE = "cost";
	public static final String ACCOUNTNUMERS = "account";
	public static final String YEAR = "year";
	public static final String MONTH = "month";

	public DBObject[] runGetData(String[] orgCodeArray, String[] costCodeArray,
			String[] costElementArray, int year, int month, String[] account,
			String targetCollection) throws Exception {
		DBObject[] costCenterRNDCostData = syncRNDCost(orgCodeArray,
				costCodeArray, costElementArray, year, month, account,
				targetCollection);
		return costCenterRNDCostData;
	}


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
	private DBObject[] syncRNDCost(String[] orgCodeArray,
			String[] costCodeArray, String[] costElementArray, int year,
			int month, String[] account, String targetCollection)
			throws Exception {
		JCO_ZXFUN_PM_YFFY func = new JCO_ZXFUN_PM_YFFY();
		Map<String, Map<String, Double>> ret = func.getJSDZB(orgCodeArray,
				costCodeArray, costElementArray, year, month, account);

		DBObject[] sr = new BasicDBObject[ret.size()];
		Iterator<String> iter = ret.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String costCenterCode = iter.next();
			sr[i] = new BasicDBObject();
			sr[i].put(RNDPeriodCost.F_COSTCENTERCODE, costCenterCode);
			sr[i].put(RNDPeriodCost.F_MONTH, new Integer(month));
			sr[i].put(RNDPeriodCost.F_YEAR, new Integer(year));
			sr[i].put(RNDPeriodCost.F__CDATE, new Date());
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
