package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.ProjectProvider;
import com.sg.business.visualization.chart.ProjectChartFactory;
import com.sg.widgets.birtcharts.ChartCanvas;

public class DashDepartment extends DashWidgetView {
	
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
//		chart.addListener(eventType, listener);
		
//		ExpandBar
		
		layout(chart, 1, 1);
		parent.layout();
	}
	
	protected Chart getChartData(ProjectProvider projectProvider) {
		return ProjectChartFactory.getDeptCombinationSchedualBar(projectProvider);
	}

}
