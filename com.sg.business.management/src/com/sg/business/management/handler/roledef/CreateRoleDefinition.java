package com.sg.business.management.handler.roledef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateRoleDefinition extends AbstractNavigatorHandler {

	private static final String TITLE = "创建角色定义";


	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		ViewerControl vc = getCurrentViewerControl(event);
		ProjectTemplate master = (ProjectTemplate) vc.getMaster();
		RoleDefinition rd = master.makeRoleDefinition(null);
		rd.addEventListener(vc);
		
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				RoleDefinition.EDITOR_ROLE_DEFINITION_CREATE);
		try {
			DataObjectDialog.openDialog(rd, (DataEditorConfigurator) conf,
					true, null, TITLE);
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
		}		
	}
}
