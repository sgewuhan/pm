package com.sg.sales.ui.labelprovider;


import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class ProjectIdLabelProvider extends ObjectIdLabelProvider {

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Project.class;
	}


}
