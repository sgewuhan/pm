package com.sg.business.work.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class StartTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			CurrentAccountContext context = new CurrentAccountContext();
			try {
				WorkflowSynchronizer sync = new WorkflowSynchronizer();
				sync.synchronizeUserTask(context.getAccountInfo()
						.getConsignerId(), work);
				work.doStartTask(Work.F_WF_EXECUTE, context);
				vc.getViewer().update(work, null);
			} catch (Exception e) {
				MessageUtil.showToast("开始流程任务", e);
			}
		}
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("请选择工作后执行开始流程任务操作", SWT.ICON_INFORMATION);
		return super.nullSelectionContinue(part, vc, command);
	}


}
