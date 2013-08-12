package com.sg.business.management.handler.workdef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditWorkDefinition extends AbstractNavigatorHandler {

	private static final String TITLE = "编辑工作定义";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "您需要选择一个工作定义", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		WorkDefinition workdefinition = (WorkDefinition) selected;
		Shell shell = HandlerUtil.getActiveShell(event);

		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		workdefinition.addEventListener(currentViewerControl);

		Configurator conf = null;

		int type = workdefinition.getWorkDefinitionType();

		switch (type) {
		case WorkDefinition.WORK_TYPE_GENERIC:
			conf = Widgets.getEditorRegistry().getConfigurator(
					WorkDefinition.EDITOR_GENERIC_WORK);
		case WorkDefinition.WORK_TYPE_STANDLONE:
			conf = Widgets.getEditorRegistry().getConfigurator(
					WorkDefinition.EDITOR_STANDLONE_WORK);
			break;
		case WorkDefinition.WORK_TYPE_PROJECT:
			conf = Widgets.getEditorRegistry().getConfigurator(
					WorkDefinition.EDITOR_PROJECT_WORK);
			break;
		default:
			break;
		}
		if (conf != null) {
			try {
				DataObjectDialog.openDialog(workdefinition,
						(DataEditorConfigurator) conf, true, null, TITLE);
			} catch (Exception e) {
				MessageUtil.showToast(shell, TITLE, e.getMessage(),
						SWT.ICON_ERROR);
			}
		}

		workdefinition.removeEventListener(currentViewerControl);
	}

}
