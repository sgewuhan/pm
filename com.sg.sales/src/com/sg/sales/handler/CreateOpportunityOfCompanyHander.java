package com.sg.sales.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Opportunity;
import com.sg.sales.model.TeamControl;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.viewer.ViewerControl;

public class CreateOpportunityOfCompanyHander extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		Opportunity opportunity = ModelService.createModelObject(Opportunity.class);
		opportunity.setValue(Opportunity.F_COMPANY_ID, selected.get_id());
		TeamControl.duplicateTeam(selected,opportunity);
		
		try {
			DataObjectDialog.openDialog(opportunity, "sales.opportunity.editor.create", true, null);
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

}
