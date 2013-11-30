package com.tmt.jszx.field.control;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.part.editor.fields.INavigatorSelectorControl;

public class DeptSelectControlOfJSZX implements INavigatorSelectorControl {

	public DeptSelectControlOfJSZX() {
	}

	@Override
	public boolean isSelectEnable(IStructuredSelection is) {
		if(is==null||is.isEmpty()){
			return false;
		}
		Object element = is.getFirstElement();
		if(element instanceof Organization){
			Organization org =  (Organization)element;
			Role chiefEngineer = org.getRole(IRoleConstance.ROLE_CHIEF_ENGINEER_ID, Organization.ROLE_SEARCH_UP);
			Role deputyDirector = org.getRole(IRoleConstance.ROLE_DEPUTY_DIRECTOR_ID, Organization.ROLE_SEARCH_UP);
			Role director = org.getRole(Role.ROLE_DEPT_MANAGER_ID, Organization.ROLE_SEARCH_UP);
			if(chiefEngineer==null||deputyDirector==null||director==null){
				return false;
			}
		} 
		return true;
	}
}
