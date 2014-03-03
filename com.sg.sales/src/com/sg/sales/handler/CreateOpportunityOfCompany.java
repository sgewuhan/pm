package com.sg.sales.handler;

import com.mobnut.db.model.PrimaryObject;
import com.sg.sales.model.ISalesTeam;
import com.sg.sales.model.Opportunity;
import com.sg.widgets.command.CreateInParentViewerControl;

public class CreateOpportunityOfCompany extends CreateInParentViewerControl {

	@Override
	protected void initValue(PrimaryObject parent, PrimaryObject po) {
		po.setValue(Opportunity.F_COMPANY_ID, parent.get_id());
		po.setValue(ISalesTeam.F_CUSTOMER_REP,
				parent.getValue(ISalesTeam.F_CUSTOMER_REP));
		po.setValue(ISalesTeam.F_SALES_MANAGER,
				parent.getValue(ISalesTeam.F_SALES_MANAGER));
		po.setValue(ISalesTeam.F_SALES_SUP,
				parent.getValue(ISalesTeam.F_SALES_SUP));
		po.setValue(ISalesTeam.F_SERVICE_MANAGER,
				parent.getValue(ISalesTeam.F_SERVICE_MANAGER));
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
