package com.sg.business.management.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

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
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "����Ҫѡ��һ����ɫ����", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		final IWorkbenchPart part = HandlerUtil.getActivePart(event);

		Shell shell = HandlerUtil.getActiveShell(event);
		
		if(selected instanceof AbstractRoleDefinition){
			AbstractRoleDefinition rd = (AbstractRoleDefinition) selected;
			if(rd.isSystemRole()){
				MessageUtil.showToast("������ɾ��ϵͳ��ɫ", SWT.ICON_WARNING);
				return;
			}
		}
		
		int yes = MessageUtil.showMessage(shell, TITLE,
				"��ȷ��Ҫɾ�������ɫ������\n�ò��������ɻָ���ѡ��YESȷ��ɾ����", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if(yes!=SWT.YES){
			return;
		}
		
		
		ViewerControl vc = getCurrentViewerControl(event);
		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
			if (part instanceof INavigatorActionListener) {
				//֪ͨ�༭�������˸��ģ������༭��������ҳ����Խ�����Ӧ
				sendNavigatorActionEvent(
						(INavigatorActionListener) part,
						INavigatorActionListener.CREATE,
						new Integer(
								INavigatorActionListener.REFRESH));
			}
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

}
