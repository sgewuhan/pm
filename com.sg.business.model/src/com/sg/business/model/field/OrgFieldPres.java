package com.sg.business.model.field;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.valuepresentation.OidOrDBObjectFieldPresentation;

public class OrgFieldPres extends OidOrDBObjectFieldPresentation {

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Organization.class;
	}


}
