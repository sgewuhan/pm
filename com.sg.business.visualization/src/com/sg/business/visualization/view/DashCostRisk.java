package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.chart.ProjectChartFactory;

public class DashCostRisk extends DashSingleChart {

	@Override
	protected Chart getChartData(ProjectProvider projectProvider) {
		return ProjectChartFactory.getProcessProjectBudgetAndCostMeter(projectProvider);
	}

}
