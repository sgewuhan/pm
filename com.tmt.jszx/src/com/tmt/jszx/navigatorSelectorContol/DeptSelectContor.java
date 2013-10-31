package com.tmt.jszx.navigatorSelectorContol;

import org.eclipse.jface.viewers.IStructuredSelection;

import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.taskforms.IRoleConstance;
import com.sg.widgets.part.editor.fields.INavigatorSelectorControl;

public class DeptSelectContor implements INavigatorSelectorControl {

	public DeptSelectContor() {
	}

	@Override
	public boolean isSelectEnable(IStructuredSelection is) {
		if(is==null||is.isEmpty()){
			return false;
		}
		Object element = is.getFirstElement();
		if(element instanceof Organization){
			Organization org =  (Organization)element;
			Role chiefEngineer = org.getRole(IRoleConstance.ROLE_CHIEF_ENGINEER_ID, 1);
			Role deputyDirector = org.getRole(IRoleConstance.ROLE_DEPUTY_DIRECTOR_ID, 1);
			Role director = org.getRole(IRoleConstance.ROLE_DIRECTOR_ID, 1);
			if(chiefEngineer==null||deputyDirector==null||director==null){
				return false;
			}
		} 
		return true;
	}
}
