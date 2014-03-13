package com.sg.sales.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Income;
import com.sg.widgets.command.CreateInParentViewerControl;

public class CreateIncomeOfContract extends  CreateInParentViewerControl {


	@Override
	protected String getEnd2Key() {
		return Income.F_CONTRACT_ID;
	}

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Income.class;
	}

}
