package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IActivateSwitch;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class SwitchActivation extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		IActivateSwitch iActivateSwitch = selected
				.getAdapter(IActivateSwitch.class);
		if (iActivateSwitch != null) {
			try {
				iActivateSwitch.doSwitchActivation(new CurrentAccountContext());
				vc.getViewer().update(selected, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
