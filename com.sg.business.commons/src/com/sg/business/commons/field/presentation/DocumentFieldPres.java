package com.sg.business.commons.field.presentation;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.widgets.commons.valuepresentation.OidOrDBObjectFieldPresentation;

public class DocumentFieldPres extends OidOrDBObjectFieldPresentation {

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Document.class;
	}

}
