package com.sg.sales.handler;

import com.sg.sales.model.Contact;
import com.sg.widgets.command.AddValueToListField;

public class AddContactToField extends AddValueToListField{

	@Override
	protected Object getValueFromSelection(Object selected) {
		return ((Contact)selected).getValue(Contact.F__ID);
	}

	@Override
	protected String getNavigatorId() {
		return "sales.contact.selector";
	}
	

}
