package com.sg.business.project.handler.role;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveProjectRoleOrMember extends AbstractNavigatorHandler{

	private static final String TITLE = "ɾ����Ŀ��ɫ";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "����Ҫѡ��һ����ɫ���û�ָ��", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		
		int yes = MessageUtil.showMessage(shell, TITLE,
				"��ȷ��Ҫɾ����\n�ò��������ɻָ���ѡ��YESȷ��ɾ����", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if(yes!=SWT.YES){
			return;
		}
		
		
		ViewerControl vc = getCurrentViewerControl(event);
		selected.addEventListener(vc);

		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
	}

}
