package com.sg.sales.ui.labelprovider;


import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class WorkIdLabelProvider extends ObjectIdLabelProvider {


	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Work.class;
	}
	

}
