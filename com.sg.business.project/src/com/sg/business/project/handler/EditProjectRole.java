package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectRole;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class EditProjectRole extends AbstractNavigatorHandler {

	private static final String TITLE = Messages.get().EditProjectRole_0;

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();
		if (!(selected instanceof ProjectRole)) {
			MessageUtil.showToast(shell, TITLE, Messages.get().EditProjectRole_1, SWT.ICON_WARNING);
			return;
		}

		ProjectRole rd = ((ProjectRole) selected);
		if (rd.isOrganizatioRole()) {
			MessageUtil.showToast(shell, TITLE, Messages.get().EditProjectRole_2, SWT.ICON_WARNING);
			return;
		}

		selected.addEventListener(vc);

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				ProjectRole.EDITOR_ROLE_DEFINITION_EDIT);
		try {
			DataObjectDialog.openDialog(selected,
					(DataEditorConfigurator) conf, true, null, TITLE);
		} catch (Exception e) {
			MessageUtil.showToast(shell, TITLE, e.getMessage(), SWT.ICON_ERROR);
		}
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		final Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, Messages.get().EditProjectRole_3, SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}


}
