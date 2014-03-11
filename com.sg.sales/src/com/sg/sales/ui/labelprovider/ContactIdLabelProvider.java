package com.sg.sales.ui.labelprovider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Contact;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class ContactIdLabelProvider extends ObjectIdLabelProvider {


	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Contact.class;
	}

}
