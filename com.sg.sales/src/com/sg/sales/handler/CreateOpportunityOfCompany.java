package com.sg.sales.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.Opportunity;
import com.sg.sales.model.TeamControl;
import com.sg.widgets.command.CreateInParentViewerControl;

public class CreateOpportunityOfCompany extends CreateInParentViewerControl {

	@Override
	protected void initValue(PrimaryObject parent, PrimaryObject po) {
		po.setValue(Opportunity.F_COMPANY_ID, parent.get_id());
		if (parent instanceof TeamControl) {
			((TeamControl) parent).duplicateTeamTo(po);
		}
	}

	@Override
	protected String getEnd2Key() {
		return Opportunity.F_COMPANY_ID;
	}

	@Override
	protected Class<? extends PrimaryObject> getModelClass() {
		return Opportunity.class;
	}

}
