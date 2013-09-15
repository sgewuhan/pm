package com.sg.business.commons.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.commons.ui.flow.ProcessViewerDialog;
import com.sg.business.model.IProcessControlable;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class WorkflowView extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		// �ٶ�����������̿��ƶ���
		Assert.isTrue(selected instanceof IProcessControlable);
		String key = event.getParameter("process.key");

		IProcessControlable procCtl = (IProcessControlable) selected;
		ProcessViewerDialog pvd = new ProcessViewerDialog(
				HandlerUtil.getActiveShell(event), procCtl, key,""+selected+"���̼�¼");
		pvd.open();
	}

}
