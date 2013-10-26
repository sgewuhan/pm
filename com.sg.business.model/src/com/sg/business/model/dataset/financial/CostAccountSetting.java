package com.sg.business.model.dataset.financial;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class CostAccountSetting extends SingleDBCollectionDataSetFactory {

	public CostAccountSetting() {
		super(IModelConstants.DB, IModelConstants.C_COSTACCOUNT_ITEM);
	}

}
