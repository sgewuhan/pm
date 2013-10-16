package com.sg.business.commons.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.ui.flow.ProcessViewerDialog;
import com.sg.business.model.IProcessControl;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class WorkflowView extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) { // 假定传入的是流程控制对象
		IProcessControl procCtl;
		if (selected instanceof IProcessControl) {
			procCtl = (IProcessControl) selected;
		} else {
			Object adapter = selected.getAdapter(IProcessControl.class);
			if (adapter instanceof IProcessControl) {
				procCtl = (IProcessControl) adapter;
			} else {
				return;
			}
		}

		String key = (String) parameters.get("process.key");
		Shell shell = part.getSite().getShell();
		ProcessViewerDialog pvd = new ProcessViewerDialog(shell, procCtl, key,
				"" + selected + "流程记录");
		pvd.open();

	}

}
