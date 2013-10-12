package com.sg.business.management.handler;

import org.eclipse.core.commands.ExecutionEvent;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IActivateSwitch;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class SwitchActivation extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		IActivateSwitch iActivateSwitch = selected
				.getAdapter(IActivateSwitch.class);
		if (iActivateSwitch != null) {
			try {
				iActivateSwitch.doSwitchActivation(new CurrentAccountContext());
				ViewerControl vc = getCurrentViewerControl(event);
				vc.getViewer().update(selected, null);
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}
	}

}
