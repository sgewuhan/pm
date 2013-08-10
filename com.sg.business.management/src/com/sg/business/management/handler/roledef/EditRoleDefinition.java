package com.sg.business.management.handler.roledef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditRoleDefinition extends AbstractNavigatorHandler {

	private static final String TITLE = "编辑角色定义";
	
	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		
		RoleDefinition rd = ((RoleDefinition)selected);
		if(rd.isOrganizatioRole()){
			MessageUtil.showToast(shell, TITLE, "您需要选择一个角色定义", SWT.ICON_WARNING);
			return;
		}
		
		
		ViewerControl vc = getCurrentViewerControl(event);
		selected.addEventListener(vc);
		
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				RoleDefinition.EDITOR_ROLE_DEFINITION_CREATE);
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
		MessageUtil.showToast(shell, TITLE, "您需要选择一个角色定义", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

}
