package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.ProjectProvider;
import com.sg.widgets.birtcharts.ChartCanvas;

public abstract class DashSingleChart extends DashWidgetView {

	private ChartCanvas chart;

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		chart = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {
				return getChartData(projectProvider);
			}
		};

		layout(chart, 1, 1);
		parent.layout();
	}

	protected abstract Chart getChartData(ProjectProvider projectProvider);

}
