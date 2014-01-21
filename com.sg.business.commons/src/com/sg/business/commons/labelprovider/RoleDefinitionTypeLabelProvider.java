package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.AbstractRoleDefinition;
import com.sg.business.resource.nls.Messages;

public class RoleDefinitionTypeLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(((AbstractRoleDefinition)element).isOrganizatioRole()){
			return Messages.get().RoleDefinitionTypeLabelProvider_0;
		}else{
			return Messages.get().RoleDefinitionTypeLabelProvider_1;
		}
	}

}
