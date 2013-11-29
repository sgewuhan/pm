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

public class RNDPeriodCostAdapter extends BasicPeriodCostAdapter{

	@Override
	public DBObject[] runGetData(String[] orgCodeArray, String[] costCodeArray,
			String[] costElementArray, int year, int month, String[] account,
			String targetCollection) throws Exception {
		DBObject[] result = super.runGetData(orgCodeArray,
				costCodeArray, costElementArray, year, month, account,
				targetCollection);

		// 分摊至每个项目的工作令号
		for (int i = 0; i < result.length; i++) {
			String costCenterCode = (String) result[i]
					.get(RNDPeriodCost.F_COSTCENTERCODE);
			allocateToWorkOrder(costCenterCode, year, month,
					result[i]);
		}
		return result;
	}

	private void allocateToWorkOrder(String costCenterCode, int year,
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
