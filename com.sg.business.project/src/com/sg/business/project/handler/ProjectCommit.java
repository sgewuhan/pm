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
				int i = MessageUtil.showMessage(shell, "�ƻ��ύ",
						"�ƻ��ύ�����Ѿ����������Ƿ���Ҫ������ʼ�ù�����" + "\n"
								+ "ѡ��YES�����̿�ʼ�ύ�������������̡�" + "\n"
								+ "ѡ��NO,���������ҵĹ��������±༭�����Ժ��ύ��", SWT.YES | SWT.NO
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
