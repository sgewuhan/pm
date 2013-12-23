package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sg.business.visualization.chart.ProjectChartFactory;
import com.sg.widgets.birtcharts.ChartCanvas;

public class IndicatorsView extends AbstractDashWidgetView {

	private ChartCanvas projectSchedualMeter;
	private ChartCanvas projectBudgetControlMeter;

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new GridLayout(2, true));

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

		layout(projectSchedualMeter, 1, 1);
		layout(projectBudgetControlMeter, 1, 1);
		parent.layout();
	}
}
