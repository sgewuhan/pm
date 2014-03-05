package com.sg.sales.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.POItem;
import com.sg.widgets.command.CreateInParentViewerControl;

public class CreatePOIOfContract extends  CreateInParentViewerControl {


	@Override
	protected String getEnd2Key() {
		return POItem.F_CONTRACT_ID;
	}

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return POItem.class;
	}

}
