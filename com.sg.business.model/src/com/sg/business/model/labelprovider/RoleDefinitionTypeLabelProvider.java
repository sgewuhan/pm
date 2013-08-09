package com.sg.business.model.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.RoleDefinition;

public class RoleDefinitionTypeLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if(((RoleDefinition)element).isOrganizatioRole()){
			return "��֯��ɫ";
		}else{
			return "��Ŀ��ɫ";
		}
	}

}
