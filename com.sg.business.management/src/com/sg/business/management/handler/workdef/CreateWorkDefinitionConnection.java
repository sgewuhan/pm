package com.sg.business.management.handler.workdef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateWorkDefinitionConnection extends AbstractNavigatorHandler {

	private static final String TITLE = "创建工作顺序关系";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		
		Shell shell = HandlerUtil.getActiveShell(event);
		ViewerControl vc = getCurrentViewerControl(event);
		ProjectTemplate master = (ProjectTemplate) vc.getMaster();
		WorkDefinitionConnection wdc = master.makeWorkDefinitionConnection();
		wdc.addEventListener(vc);
		
		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				WorkDefinitionConnection.EDITOR);
		try {
			DataObjectDialog.openDialog(wdc, (DataEditorConfigurator) conf,
					true, null, TITLE);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
		}
	}

}
