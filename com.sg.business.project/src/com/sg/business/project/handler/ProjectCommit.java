package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.business.project.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectCommit extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof Project) {
			Project project = (Project) selected;
			try {
				doCommit(project, new CurrentAccountContext());
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.showToast(e);
			}
			vc.getViewer().update(selected, null);
			vc.getViewer().setSelection(null);
		}
	}

	/**
	 * 提交项目计划<br/>
	 * 判断项目计划是否定义了提交流程，如果定义了提交流程，使用流程进行提交 。<br>
	 * 如果没有定义流程，直接发送消息。<br/>
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void doCommit(Project project, IContext context) throws Exception {
		project.checkCommitAction(context);
		if (project.isCommitWorkflowActivate()) {
			Work work = project.makeWorkflowCommitableWork(null, context);
			// 打开定义的用于启动工作的对话框编辑器
			DataObjectWizard dw = DataObjectWizard.open(work,
					Work.EDITOR_LAUNCH, true, null);
			if (dw.getResult() == Window.OK) {

				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				int i = MessageUtil.showMessage(shell, Messages.get().ProjectCommit_0,
						Messages.get().ProjectCommit_1 + "\n" //$NON-NLS-2$
								+ Messages.get().ProjectCommit_3 + "\n" //$NON-NLS-2$
								+ Messages.get().ProjectCommit_5, SWT.YES | SWT.NO
								| SWT.ICON_QUESTION);
				if (i == SWT.YES) {
					// 在该编辑器确定后启动工作
					work.doStart(context);
					project.doCommitWithWork(work);
				} else {
				}
			}
		} else {
			project.doCommitWithSendMessage(context);
		}
	}


}
