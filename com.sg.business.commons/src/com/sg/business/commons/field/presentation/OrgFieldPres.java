package com.sg.business.commons.field.presentation;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.valuepresentation.OidOrDBObjectFieldPresentation;

/**
 * 设置部门显示样式，为部门简称
 * @author gdiyang
 *
 */
public class OrgFieldPres extends OidOrDBObjectFieldPresentation {

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Organization.class;
	}


}
