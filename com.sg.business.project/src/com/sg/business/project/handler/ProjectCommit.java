package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectCommit extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Project) {
			ViewerControl vc = getCurrentViewerControl(event);
			Project project = (Project) selected;
			try {
				doCommit(project, new CurrentAccountContext());
				vc.getViewer().update(selected, null);

			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
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
				int i = MessageUtil.showMessage(shell, "计划提交",
						"计划提交工作已经创建，您是否需要立即开始该工作？" + "\n"
								+ "选择YES将立刻开始提交工作并启动流程。" + "\n"
								+ "选择NO,您可以在我的工作中重新编辑并在以后提交。", SWT.YES | SWT.NO
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
