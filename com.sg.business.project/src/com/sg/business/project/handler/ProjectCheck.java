package com.sg.business.project.handler;

import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.project.nls.Messages;
import com.sg.business.project.view.ProjectCheckView;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.viewer.ViewerControl;

public class ProjectCheck extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl currentViewerControl, Command command,
			Map<String, Object> parameters, IStructuredSelection selection) {
		if (selected instanceof Project) {
			Project project = (Project) selected;
			List<ICheckListItem> result = project.checkPlan();

			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				ProjectCheckView part1 = (ProjectCheckView) page
						.showView("project.view.check"); //$NON-NLS-1$
				part1.setInput(result);
				MessageUtil.showToast(Messages.get().ProjectCheck_1,
						SWT.ICON_INFORMATION);
			} catch (PartInitException e) {
				MessageUtil.showToast(e);
			}
		}
	}


}
