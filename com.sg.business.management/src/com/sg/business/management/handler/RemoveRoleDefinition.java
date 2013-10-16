package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractRoleDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.INavigatorActionListener;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveRoleDefinition extends AbstractNavigatorHandler {
	private static final String TITLE = "ɾ����ɫ����";

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, "����Ҫѡ��һ����ɫ����", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {

		Shell shell = part.getSite().getShell();

		if (selected instanceof AbstractRoleDefinition) {
			AbstractRoleDefinition rd = (AbstractRoleDefinition) selected;
			if (rd.isSystemRole()) {
				MessageUtil.showToast("������ɾ��ϵͳ��ɫ", SWT.ICON_WARNING);
				return;
			}
		}

		int yes = MessageUtil.showMessage(shell, TITLE,
				"��ȷ��Ҫɾ�������ɫ������\n�ò��������ɻָ���ѡ��YESȷ��ɾ����", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if (yes != SWT.YES) {
			return;
		}

		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
			if (part instanceof INavigatorActionListener) {
				// ֪ͨ�༭�������˸��ģ������༭��������ҳ����Խ�����Ӧ
				sendNavigatorActionEvent((INavigatorActionListener) part,
						INavigatorActionListener.CREATE, new Integer(
								INavigatorActionListener.REFRESH));
			}
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

}
