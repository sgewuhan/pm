package com.sg.business.visualization.editor.project;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.ProjectChartFactory;
import com.sg.business.visualization.nls.Messages;
import com.sg.widgets.birtcharts.ChartCanvas;


public class ProjectDashboardDesInv extends AbstractProjectPage {

	@Override
	protected void createGraphic(Composite parent) {
		new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {
				return ProjectChartFactory.getProjectDesignatedInvestmentBar(project);
			}
		};
	}

	@Override
	protected String getPageTitle() {
		return Messages.get().ProjectDashboardDesInv_0;
	}



}
