package com.sg.business.commons.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.AbstractRoleDefinition;

public class RoleDefinitionTypeLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(((AbstractRoleDefinition)element).isOrganizatioRole()){
			return "组织角色";
		}else{
			return "项目角色";
		}
	}

}
