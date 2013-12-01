package com.sg.business.visualization.view.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.UserProjectPerf;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.registry.config.Configurator;
import com.sg.widgets.registry.config.DataEditorConfigurator;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectSetAdd extends AbstractNavigatorHandler {

	private static final String TITLE = "设定项目集";

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		final Shell shell = part.getSite().getShell();
		MessageUtil.showToast(shell, TITLE, "您需要选择一个项目", SWT.ICON_WARNING);
		return super.nullSelectionContinue(part, vc, command);
	}

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		final Shell shell = part.getSite().getShell();
		Assert.isNotNull(currentViewerControl);
//TODO 需要支持添加多个
		if (selected instanceof Project) {
			Project project = (Project) selected;
			project.addEventListener(currentViewerControl);
			Configurator conf = Widgets.getEditorRegistry().getConfigurator(
					UserProjectPerf.EDITOR_SETTING);
			try {
				UserProjectPerf pperf=project.makeUserProjectPerf();
				pperf.setValue(UserProjectPerf.F_USERID, new CurrentAccountContext().getUserId());
				DataObjectDialog.openDialog(pperf,
						(DataEditorConfigurator) conf, true, null, TITLE);
			} catch (Exception e) {
				MessageUtil.showToast(shell, TITLE, e.getMessage(),
						SWT.ICON_ERROR);
			}

			project.removeEventListener(currentViewerControl);
		}

	}

}
