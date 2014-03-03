package com.sg.sales.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Contact;
import com.sg.widgets.command.CreateInParentViewerControl;

public class CreateContactOfCompany extends  CreateInParentViewerControl {


	@Override
	protected String getEnd2Key() {
		return Contact.F_COMPANY_ID;
	}

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Contact.class;
	}

}
