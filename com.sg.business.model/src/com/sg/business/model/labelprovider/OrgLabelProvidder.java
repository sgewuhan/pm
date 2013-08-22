package com.sg.business.model.labelprovider;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class OrgLabelProvidder extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		Object value = getValue(element);
		if(value instanceof ObjectId){
			Organization org = ModelService.createModelObject(Organization.class, (ObjectId)value);
			return org.getLabel();
		}
		return "";
	}
}
