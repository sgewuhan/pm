package com.sg.business.management.handler.workdef;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.view.NavigatorPart;

public class ChangeToGenericWorkDefinition extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, ExecutionEvent event) {
		WorkDefinition workd = (WorkDefinition) selected;
		try {
			Organization organization = workd.getOrganization();
			ProjectTemplate projectTemplate = workd.getProjectTemplate();
			if (projectTemplate != null) {
				organization = projectTemplate.getOrganization();
			}
			Assert.isNotNull(organization);

			WorkDefinition gWorkd = organization
					.makeGenericWorkDefinition(null);
			workd.doExportGenericWorkDefinition(gWorkd,
					new CurrentAccountContext());

			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			NavigatorPart np = (NavigatorPart) page
					.findView("management.genericwork.definitions");
			np.reloadMaster();

		} catch (Exception e) {
			MessageUtil.showToast(e.getMessage(), SWT.ICON_WARNING);
		}
	}

}
