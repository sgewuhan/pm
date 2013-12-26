package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.visualization.chart.LineChart;
import com.sg.business.visualization.nls.Messages;
import com.sg.widgets.birtcharts.ChartCanvas;

public class CombinnatiedRateView extends AbstractDashWidgetView {

	private ChartCanvas chart;
	
	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		chart = new ChartCanvas(parent, SWT.NONE) {
			@Override
			public Chart getChart() throws Exception {

				return CombinnatiedRateView.this.getChart();
			}
		};
		layout(chart, 1, 1);
		parent.layout();
	}

	private Chart getChart() throws Exception {
		Messages messages = Messages.get(chart.getDisplay());
		String[] lsText = { messages.CombinnatiedRateView_A_0, messages.CombinnatiedRateView_A_1 };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"8", "9", "10", "11", "12" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		double[] value1 = projectProvider.getRateValueByYear("overcost_def"); //$NON-NLS-1$
		double[] value2 = projectProvider.getRateValueByYear("isdelay_def"); //$NON-NLS-1$

		return LineChart.getChart(xAxisText, lsText, new double[][] { value1,
				value2 }, -2);
	}

}
