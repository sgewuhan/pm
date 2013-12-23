package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.BarChart;
import com.sg.widgets.birtcharts.ChartCanvas;

public class OverSchedualVolumnView extends AbstractDashWidgetView {

	public OverSchedualVolumnView() {
	}

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new FillLayout());

		new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() throws Exception {

				return OverSchedualVolumnView.this.getChart();
			}
		};
	}

	private Chart getChart() throws Exception {
		String[] bsText = { "正常项目数", "超期项目数" };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };

		double[][] value1 = projectProvider.getDelayValueByYear();

		return BarChart.getChart(xAxisText, bsText, value1, "Stacked", -3);
	}

}
