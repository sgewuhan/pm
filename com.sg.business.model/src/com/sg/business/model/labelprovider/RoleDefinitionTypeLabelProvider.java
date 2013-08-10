package com.sg.business.model.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.RoleDefinition;

public class RoleDefinitionTypeLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(((RoleDefinition)element).isOrganizatioRole()){
			return "组织角色";
		}else{
			return "项目角色";
		}
	}

}
