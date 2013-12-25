package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.BarChart;
import com.sg.widgets.birtcharts.ChartCanvas;

public class ProfitVolumnView extends AbstractDashWidgetView {

	private ChartCanvas chart;

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		chart =new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() throws Exception {

				return ProfitVolumnView.this.getChart();
			}
		};
		layout(chart, 1, 1);
		parent.layout();
	}

	private Chart getChart() throws Exception {
		String[] bsText = { "销售利润", "销售成本" };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };

		double[][] value1 = projectProvider.getProfitAndCostByYear();

		return BarChart.getChart(xAxisText, bsText, value1, "Side-by-side", -5);
	}

}
