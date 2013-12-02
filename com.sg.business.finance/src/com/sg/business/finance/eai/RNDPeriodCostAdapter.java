package com.sg.business.finance.eai;

import java.util.HashMap;
import java.util.Map;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.RNDPeriodCost;

public class RNDPeriodCostAdapter extends BasicPeriodCostAdapter {

	public DBObject[] runGetData(String[] costCodeArray,
			String[] costElementArray, int year, int month) throws Exception {
		DBObject[] result = runGetData(costCodeArray, null, costElementArray,
				year, month, IModelConstants.C_RND_PEROIDCOST_COSTCENTER);

		// 分摊至每个项目的工作令号
		for (int i = 0; i < result.length; i++) {
			String costCenterCode = (String) result[i]
					.get(RNDPeriodCost.F_COSTCENTERCODE);

			allocateToWorkOrder2(costCenterCode, year, month, result[i]);
		}
		return result;
	}
	
	
	
	/**
	 * 导入期初数据时使用，不考虑工时记录直接根据项目进行分摊
	 * @param costCenterCode
	 * @param year
	 * @param month
	 * @param dbObject
	 */
	public void allocateToWorkOrder2(String costCenterCode, int year,
			int month, DBObject costCenterRNDCostData) {
		//获得成本中心对应的组织
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject orgData = col.findOne(new BasicDBObject().append(
				Organization.F_COST_CENTER_CODE, costCenterCode));
		Organization organization = ModelService.createModelObject(orgData,
				Organization.class);

		//初始化工作令号期间数据分摊适配器
		WorkorderPeriodCostAllocate2 adapter = new WorkorderPeriodCostAllocate2();

		Map<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(WorkorderPeriodCostAllocate2.YEAR, year);
		parameter.put(WorkorderPeriodCostAllocate2.MONTH, month);
		parameter.put(WorkorderPeriodCostAllocate2.COSECENTERCODE,
				organization.getCostCenterCode());
		RNDPeriodCost rndPeriodCost = ModelService.createModelObject(
				costCenterRNDCostData, RNDPeriodCost.class);
		parameter.put(WorkorderPeriodCostAllocate2.RNDCOST, rndPeriodCost);

		adapter.getData(parameter);
	}




	public void allocateToWorkOrder(String costCenterCode, int year,
			int month, DBObject costCenterRNDCostData) {
		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject orgData = col.findOne(new BasicDBObject().append(
				Organization.F_COST_CENTER_CODE, costCenterCode));

		Organization organization = ModelService.createModelObject(orgData,
				Organization.class);

		WorkorderPeriodCostAllocate adapter = new WorkorderPeriodCostAllocate();

		Map<String, Object> parameter = new HashMap<String, Object>();

		parameter.put(WorkorderPeriodCostAllocate.YEAR, year);
		parameter.put(WorkorderPeriodCostAllocate.MONTH, month);
		parameter.put(WorkorderPeriodCostAllocate.COSECENTERCODE,
				organization.getCostCenterCode());
		RNDPeriodCost rndPeriodCost = ModelService.createModelObject(
				costCenterRNDCostData, RNDPeriodCost.class);
		parameter.put(WorkorderPeriodCostAllocate.RNDCOST, rndPeriodCost);

		adapter.getData(parameter);

	}

}
