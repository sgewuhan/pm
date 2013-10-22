package com.sg.business.performence.works;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class PerformenceWorkLabelProvider extends ObjectIdLabelProvider {


	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Work.class;
	}

}
