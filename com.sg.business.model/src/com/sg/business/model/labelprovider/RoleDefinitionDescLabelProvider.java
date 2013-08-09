package com.sg.business.model.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.RoleDefinition;

public class RoleDefinitionDescLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		RoleDefinition roled = (RoleDefinition)element;
		if(roled.isOrganizatioRole()){
			Role role = roled.getOrganizationRole();
			Organization org = role.getOrganization();
			return org.getPath();
		}else{
			return "ÏîÄ¿½ÇÉ«";
		}
	}

}
