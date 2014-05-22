package com.sg.business.project.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.DummyWork;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class SystemAdminUpdateWork extends AbstractNavigatorHandler {
	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		MessageUtil.showToast("请选中工作", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Work) {
			Work work = (Work) selected;
			DummyWork dummyWork = work.getAdapter(DummyWork.class);
			Configurator conf = Widgets.getEditorRegistry().getConfigurator(
					"edit.work.systemadminupdate");
			try {
				DataObjectDialog.openDialog(dummyWork,
						(DataEditorConfigurator) conf, true, null,
						dummyWork.getTypeName());
				vc.getViewer().refresh();
			} catch (Exception e) {
				MessageUtil.showToast(e);
			}
		}

	}

}
