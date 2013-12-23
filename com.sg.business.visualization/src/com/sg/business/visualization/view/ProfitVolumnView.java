package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.BarChart;
import com.sg.widgets.birtcharts.ChartCanvas;

public class ProfitVolumnView extends AbstractDashWidgetView {

	public ProfitVolumnView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new FillLayout());

		new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() throws Exception {

				return ProfitVolumnView.this.getChart();
			}
		};
	}

	private Chart getChart() throws Exception {
		String[] bsText = { "��������", "���۳ɱ�" };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12" };

		double[][] value1 = projectProvider.getProfitAndCostByYear();

		return BarChart.getChart(xAxisText, bsText, value1, "Side-by-side", -5);
	}

}
