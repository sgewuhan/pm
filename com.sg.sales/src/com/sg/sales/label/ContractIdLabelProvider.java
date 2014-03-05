package com.sg.sales.label;


import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Contract;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class ContractIdLabelProvider extends ObjectIdLabelProvider {

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Contract.class;
	}


}
