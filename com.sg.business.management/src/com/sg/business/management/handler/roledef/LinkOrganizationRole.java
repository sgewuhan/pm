package com.sg.business.management.handler.roledef;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.commons.selector.NavigatorSelector;

public class LinkOrganizationRole extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		NavigatorSelector.OPEN("management.roleselector");
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}

}
