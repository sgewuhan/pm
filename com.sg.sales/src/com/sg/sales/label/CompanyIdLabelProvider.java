package com.sg.sales.label;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Company;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class CompanyIdLabelProvider extends ObjectIdLabelProvider {


	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Company.class;
	}


}
