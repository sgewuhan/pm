package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class ProjectCheck extends AbstractNavigatorHandler {


	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if(selected instanceof Project){
			Project project = (Project) selected;
			project.checkPlan();
		}
	}

}
