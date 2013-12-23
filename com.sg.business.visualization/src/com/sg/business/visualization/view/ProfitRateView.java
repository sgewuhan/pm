package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.LineChart;
import com.sg.widgets.birtcharts.ChartCanvas;

public class ProfitRateView extends AbstractDashWidgetView {

	public ProfitRateView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new FillLayout());

		new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() throws Exception {

				return ProfitRateView.this.getChart();
			}
		};
	}

	private Chart getChart() throws Exception {
		String[] lsText = { "项目利润率" };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };

		double[] value1 = projectProvider.getProfitRateByYear();

		return LineChart.getChart(xAxisText, lsText, new double[][] { value1 },
				-4);
	}

}
