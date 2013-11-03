package com.sg.business.work.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.business.work.WorkflowSynchronizer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class StartTask extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Work) {
			
			
			
			try {
				Work work = (Work) selected;
				WorkflowSynchronizer sync = new WorkflowSynchronizer();
				CurrentAccountContext context = new CurrentAccountContext();
				String userid = context.getAccountInfo().getConsignerId();
				List<UserTask> userTasks = sync.synchronizeUserTask(userid,
						work);

				if (userTasks.isEmpty()) {
					MessageUtil.showToast("没有您需要执行的流程任务", SWT.ICON_INFORMATION);
					return;
				}

				if (userTasks.size() > 1) {
					// TODO 显示选择器选择流程
					work.doStartTask(Work.F_WF_EXECUTE, userTasks.get(0),
							context);
				} else {
					work.doStartTask(Work.F_WF_EXECUTE, userTasks.get(0),
							context);
				}

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
