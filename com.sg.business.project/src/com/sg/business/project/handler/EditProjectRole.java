package com.sg.business.project.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectRole;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditProjectRole extends AbstractNavigatorHandler {

	private static final String TITLE = "�༭��ɫ";
	
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		if(!(selected instanceof ProjectRole)){
			MessageUtil.showToast(shell, TITLE, "��ֻ�ܱ༭��Ŀ��ɫ", SWT.ICON_WARNING);
			return;
		}

		ProjectRole rd = ((ProjectRole)selected);
		if(rd.isOrganizatioRole()){
			MessageUtil.showToast(shell, TITLE, "��ֻ�ܱ༭��Ŀ��ɫ", SWT.ICON_WARNING);
			return;
		}
		
		
		ViewerControl vc = getCurrentViewerControl(event);
		selected.addEventListener(vc);
		
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				ProjectRole.EDITOR_ROLE_DEFINITION_EDIT);
		try {
			DataObjectDialog.openDialog(selected, (DataEditorConfigurator) conf,
					true, null, TITLE);
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
		}			
	}

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "����Ҫѡ��һ����ɫ����", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

}
