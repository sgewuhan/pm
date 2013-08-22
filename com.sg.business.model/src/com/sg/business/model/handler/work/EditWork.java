package com.sg.business.model.handler.work;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.AbstractWork;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditWork extends AbstractNavigatorHandler {


	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		MessageUtil.showToast("您需要选择一项", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		AbstractWork workdefinition = (AbstractWork) selected;
		Shell shell = HandlerUtil.getActiveShell(event);

		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		workdefinition.addEventListener(currentViewerControl);

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				selected.getDefaultEditorId());

		if (conf != null) {
			try {
				DataObjectDialog.openDialog(workdefinition,
						(DataEditorConfigurator) conf, true, null, "编辑"+selected.getTypeName());
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.showToast(shell, "编辑"+selected.getTypeName(), e.getMessage(),
						SWT.ICON_ERROR);
			}
		}

		workdefinition.removeEventListener(currentViewerControl);
	}

}
