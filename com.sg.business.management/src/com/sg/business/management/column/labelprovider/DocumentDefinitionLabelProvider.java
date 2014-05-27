package com.sg.business.management.column.labelprovider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.DocumentDefinition;
import com.sg.widgets.commons.labelprovider.ObjectIdLabelProvider;

public class DocumentDefinitionLabelProvider extends ObjectIdLabelProvider {

	public DocumentDefinitionLabelProvider() {
	}

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return DocumentDefinition.class;
	}

}
