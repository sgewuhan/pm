package com.sg.business.model.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class ChargerRoleDefOfWorkDefLabelProvider extends ConfiguratorColumnLabelProvider {

	@Override
	public String getText(Object element) {
		WorkDefinition workd = (WorkDefinition) element;
		RoleDefinition chargerRoleDef = workd.getChargerRoleDefinition(RoleDefinition.class);
		if(chargerRoleDef!=null){
			return chargerRoleDef.getLabel();
		}else{
			return "";
		}
	}
	
	@Override
	public Image getImage(Object element) {
		WorkDefinition workd = (WorkDefinition) element;
		RoleDefinition chargerRoleDef = workd.getChargerRoleDefinition(RoleDefinition.class);
		if(chargerRoleDef!=null){
			return chargerRoleDef.getImage();
		}else{
			return null;
		}
	}

}
