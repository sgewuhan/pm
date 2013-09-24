package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.ui.flow.ProcessViewerDialog;
import com.sg.business.model.IProcessControl;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class WorkflowView extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		// 假定传入的是流程控制对象
		IProcessControl procCtl;
		if (selected instanceof IProcessControl) {
			procCtl = (IProcessControl) selected;
		} else {
			Object adapter = selected.getAdapter(IProcessControl.class);
			if (adapter instanceof IProcessControl) {
				procCtl = (IProcessControl) adapter;
			}else{
				return;
			}
		}

		String key = event.getParameter("process.key");
		ProcessViewerDialog pvd = new ProcessViewerDialog(
				HandlerUtil.getActiveShell(event), procCtl, key, "" + selected
						+ "流程记录");
		pvd.open();
		
		
	}

}
