package com.sg.business.management.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.ViewerControl;

public class RemoveWorkDefinition extends AbstractWorkDefinitionHandler {

	private static final String TITLE = "ɾ����������";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "����Ҫѡ��һ����������", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(WorkDefinition selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		if(selected.getParentPrimaryObject()==null){
			MessageUtil.showToast(shell, TITLE, "�����������岻��ɾ��", SWT.ICON_WARNING);
			return;
		}
		int yes = MessageUtil.showMessage(shell, TITLE,
				"��ȷ��Ҫɾ���������������\n�ò��������ɻָ���ѡ��YESȷ��ɾ����", SWT.YES | SWT.NO
						| SWT.ICON_QUESTION);
		if(yes!=SWT.YES){
			return;
		}
		
		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		selected.addEventListener(currentViewerControl);
		try {
			selected.doRemove(new CurrentAccountContext());
		} catch (Exception e) {
			MessageUtil.showMessage(shell, TITLE, e.getMessage(),
					SWT.ICON_WARNING);
		}
		selected.removeEventListener(currentViewerControl);
	}

}
