package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.commons.ui.chart.ProjectChartFactory;
import com.sg.widgets.birtcharts.ChartCanvas;

public class DashMainIndicators extends AbstractDashWidgetView {

	private ChartCanvas projectSchedualMeter;
	private ChartCanvas projectBudgetControlMeter;
	private ChartCanvas profitRateMeter;

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new GridLayout(3, true));

		projectSchedualMeter = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory
						.getProjectSchedualMeter(projectProvider);
			}
		};

		projectBudgetControlMeter = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory
						.getFinishedProjectBudgetAndCostMeter(projectProvider);
			}
		};

		profitRateMeter = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory.getProfitRateMeter(projectProvider);
			}
		};

		layout(projectSchedualMeter, 1, 1);
		layout(projectBudgetControlMeter, 1, 1);
		layout(profitRateMeter, 1, 1);
		parent.layout();
	}

}
