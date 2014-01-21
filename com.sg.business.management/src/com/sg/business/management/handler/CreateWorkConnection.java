package com.sg.business.management.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class CreateWorkConnection extends AbstractNavigatorHandler {

	private static final String TITLE = Messages.get().CreateWorkConnection_0;

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters, IStructuredSelection selection) {
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
			MessageUtil.showToast(part.getSite().getShell(), TITLE,
					e.getMessage(), SWT.ICON_ERROR);
		}
	}

}
