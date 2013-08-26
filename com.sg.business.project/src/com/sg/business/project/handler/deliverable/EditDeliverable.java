package com.sg.business.project.handler.deliverable;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Deliverable;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditDeliverable extends AbstractNavigatorHandler {

	private static final String TITLE = "编辑交付物";

	@Override
	protected boolean nullSelectionContinue(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);
		MessageUtil.showToast(shell, TITLE, "您需要选择一个交付物", SWT.ICON_WARNING);
		return super.nullSelectionContinue(event);
	}

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveShell(event);

		ViewerControl currentViewerControl = getCurrentViewerControl(event);
		Assert.isNotNull(currentViewerControl);

		selected.addEventListener(currentViewerControl);

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				Deliverable.EDITOR);
		try {
			DataObjectDialog.openDialog(selected, (DataEditorConfigurator) conf,
					true, null, TITLE);
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
		}

		selected.removeEventListener(currentViewerControl);
	}

}
