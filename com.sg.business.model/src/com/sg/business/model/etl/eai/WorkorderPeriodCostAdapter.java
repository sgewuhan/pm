package com.sg.business.model.etl.eai;

import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;

public class WorkorderPeriodCostAdapter extends BasicPeriodCostAdapter {

	public DBObject[] runGetData(String[] workOrderArray,
			String[] costElementArray, int year, int month) throws Exception {
		DBObject[] result = runGetData(null, workOrderArray, costElementArray,
				year, month, IModelConstants.C_WORKORDER_COST);

		return result;
	}

}
