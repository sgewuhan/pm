package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.resource.nls.Messages;

public class RoleDefinitionDescLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof AbstractRoleDefinition) {

			AbstractRoleDefinition roled = (AbstractRoleDefinition) element;
			if (roled.isOrganizatioRole()) {
				Role role = roled.getOrganizationRole();
				Organization org = role.getOrganization();
				return org.getPath();
			} else {
				return Messages.get().RoleDefinitionDescLabelProvider_0;
			}
		} else {
			return ""; //$NON-NLS-1$
		}
	}

}
