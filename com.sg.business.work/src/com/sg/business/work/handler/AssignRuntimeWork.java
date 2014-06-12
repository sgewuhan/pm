package com.sg.business.work.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class AssignRuntimeWork extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		String editorId = "editor.runtimereassignment";
		Shell shell = part.getSite().getShell();
		Work work = (Work) selected;

		Configurator conf = Widgets.getEditorRegistry().getConfigurator(
				editorId);

		if (conf != null) {
			try {
				DataObjectDialog d = DataObjectDialog.openDialog(work,
						(DataEditorConfigurator) conf, true, null,
						work.getLabel());
				if (DataObjectDialog.CANCEL != d.getReturnCode()) {
					if (work.isSummaryWork()) {
						int result = MessageUtil.showMessage(shell,
								Messages.get().AssignWork_1,
								Messages.get().AssignWork_2, SWT.ICON_QUESTION
										| SWT.YES | SWT.NO);
						if (result == SWT.YES) {
							IContext context = new CurrentAccountContext();
							work.doAssignment(context);
						}
					}
					vc.getViewer().update(selected, null);
				}
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

	}

}
