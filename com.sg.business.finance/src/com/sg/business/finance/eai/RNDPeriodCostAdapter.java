package com.sg.business.finance.eai;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.sg.business.finance.eai.sap.JCO_ZXFUN_PM_YFFY;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.RNDPeriodCost;

public class RNDPeriodCostAdapter {

	public static final String ORGCODE = "org";
	public static final String COSECENTERCODE = "cost";
	public static final String ACCOUNTNUMERS = "account";
	public static final String YEAR = "year";
	public static final String MONTH = "month";

	public void runGetData(String[] orgCodeArray, String[] costCodeArray,
			String[] costElementArray, int year, int month, String[] account,
			String targetCollection) throws Exception {
		DBObject[] costCenterRNDCostData = syncRNDCost(orgCodeArray,
				costCodeArray, costElementArray, year, month, account,
				targetCollection);

		// 分摊至每个项目的工作令号
		for (int i = 0; i < costCenterRNDCostData.length; i++) {
			String costCenterCode = (String) costCenterRNDCostData[i]
					.get(RNDPeriodCost.F_COSTCENTERCODE);
			allocateToWorkOrder(costCenterCode, year, month,
					costCenterRNDCostData[i]);
		}
	}

	private void allocateToWorkOrder(String costCenterCode, int year,
			int month, DBObject costCenterRNDCostData) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject orgData = col.findOne(new BasicDBObject().append(
				Organization.F_COST_CENTER_CODE, costCenterCode));

		Organization organization = ModelService.createModelObject(orgData,
				Organization.class);

		WorkOrderPeriodCostAdapter adapter = new WorkOrderPeriodCostAdapter();

		Map<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(WorkOrderPeriodCostAdapter.YEAR, year);
		parameter.put(WorkOrderPeriodCostAdapter.MONTH, month);
		parameter.put(WorkOrderPeriodCostAdapter.COSECENTERCODE,
				organization.getCostCenterCode());
		RNDPeriodCost rndPeriodCost = ModelService.createModelObject(
				costCenterRNDCostData, RNDPeriodCost.class);
		parameter.put(WorkOrderPeriodCostAdapter.RNDCOST, rndPeriodCost);

		adapter.getData(parameter);

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
