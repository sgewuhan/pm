package com.sg.business.visualization.label.project;

import com.sg.business.model.ProjectMonthData;

public class SalesRevenue extends AbstractProjectSummary{

	@Override
	protected String getSummaryField() {
		return  ProjectMonthData.F_MONTH_SALES_REVENUE;
	}
	
}
