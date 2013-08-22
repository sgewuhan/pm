package com.sg.business.model.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;

public class RoleDefinitionDescLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		AbstractRoleDefinition roled = (AbstractRoleDefinition)element;
		if(roled.isOrganizatioRole()){
			Role role = roled.getOrganizationRole();
			Organization org = role.getOrganization();
			return org.getPath();
		}else{
			return "ÏîÄ¿½ÇÉ«";
		}
	}

}
