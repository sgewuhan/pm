package com.sg.business.visualization.label.project;

import com.sg.business.model.ProjectMonthData;

public class SalesProfit extends AbstractProjectSummary {

	@Override
	protected Object getItemValue(ProjectMonthData di) {
		Object rev = di.getValue(ProjectMonthData.F_MONTH_SALES_REVENUE);
		
		Object cost = di.getValue(ProjectMonthData.F_MONTH_SALES_COST);
		
		if(rev instanceof Number && cost instanceof Number){
			return ((Number)rev).doubleValue() - ((Number)cost).doubleValue();
		}
		return 0d;
	}

	@Override
	protected String getSummaryField() {
		return null;
	}
}
