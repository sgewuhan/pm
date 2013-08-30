package com.sg.business.project.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.check.ICheckListItem;
import com.sg.business.project.view.ProjectCheckView;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;

public class ProjectCheck extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		if (selected instanceof Project) {
			Project project = (Project) selected;
			List<ICheckListItem> result = project.checkPlan();

			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				ProjectCheckView part = (ProjectCheckView) page
						.showView("project.view.check");
				part.setInput(result);
			} catch (PartInitException e) {
				MessageUtil.showToast(e.getMessage(), SWT.ERROR);
				e.printStackTrace();
			}
		}
	}

}
