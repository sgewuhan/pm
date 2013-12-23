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
	 * �ύ��Ŀ�ƻ�<br/>
	 * �ж���Ŀ�ƻ��Ƿ������ύ���̣�����������ύ���̣�ʹ�����̽����ύ ��<br>
	 * ���û�ж������̣�ֱ�ӷ�����Ϣ��<br/>
	 * 
	 * @param context
	 * @throws Exception
	 */
	public void doCommit(Project project, IContext context) throws Exception {
		project.checkCommitAction(context);
		if (project.isCommitWorkflowActivate()) {
			Work work = project.makeWorkflowCommitableWork(null, context);
			// �򿪶�����������������ĶԻ���༭��
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
					// �ڸñ༭��ȷ������������
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
