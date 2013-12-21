package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.LineChart;
import com.sg.widgets.birtcharts.ChartCanvas;

public class CombinnatiedRateView extends AbstractDashWidgetView {

	private ChartCanvas chart;

	public CombinnatiedRateView() {
	}

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new FillLayout());

		chart = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() {

				return CombinnatiedRateView.this.getChart();
			}
		};
	}

	private Chart getChart() {
		String[] lsText = { "项目超支率", "项目超期率" };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };
		
		double[] value1 = new double[] { 25, 35, 15, 25, 35, 15, 25, 35, 15,
				25, 35, 15 };
		double[] value2 = new double[] { 10, 10, 25, 10, 10, 25, 10, 10, 25,
				10, 10, 25 };

		return LineChart.getChart(xAxisText, lsText, new double[][] { value1, value2 });
	}

	

}
