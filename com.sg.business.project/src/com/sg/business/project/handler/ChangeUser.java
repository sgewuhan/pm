package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.project.wizards.ChangeUserWizard;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class ChangeUser extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		Project po = (Project) selected;

		try {
			ChangeUserWizard.open(po);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}


}
