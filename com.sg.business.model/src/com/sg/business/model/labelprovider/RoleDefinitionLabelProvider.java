package com.sg.business.model.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class RoleDefinitionLabelProvider extends
		ConfiguratorColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		String fieldName = getFieldName();
		if(fieldName.equals(RoleDefinition.F_ROLE_NUMBER)){
			return ((RoleDefinition)element).getImage();
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		RoleDefinition roled = (RoleDefinition)element;
		PrimaryObject po;
		if(roled.isOrganizatioRole()){
			po = roled.getOrganizationRole();
		}else{
			po = roled;
		}
		String fieldName = getFieldName();
		return po.getText(fieldName);
	}


}
