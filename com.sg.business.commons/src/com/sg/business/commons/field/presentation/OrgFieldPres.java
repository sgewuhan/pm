package com.sg.business.commons.field.presentation;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.valuepresentation.OidOrDBObjectFieldPresentation;

/**
 * ���ò�����ʾ��ʽ��Ϊ���ż��
 * @author gdiyang
 *
 */
public class OrgFieldPres extends OidOrDBObjectFieldPresentation {

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Organization.class;
	}


}
