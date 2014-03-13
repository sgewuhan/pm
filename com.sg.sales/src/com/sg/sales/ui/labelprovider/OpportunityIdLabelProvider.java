package com.sg.sales.ui.labelprovider;


import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Opportunity;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class OpportunityIdLabelProvider extends ObjectIdLabelProvider {


	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Opportunity.class;
	}
	

}
