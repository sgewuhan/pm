package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.project.page.ChangeUserWizard;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class ChangeUser extends AbstractNavigatorHandler {

	
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Project po = (Project)selected;
		
		try {
			ChangeUserWizard.open(po,event);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
