package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;

import com.sg.business.visualization.chart.BarChart;
import com.sg.business.visualization.nls.Messages;

public class OverSchedualVolumnView extends AbstractDashChartView {

	@Override
	protected Chart getChartData() throws Exception {
		Messages messages = Messages.get(local);
		String[] bsText = { messages.OverSchedualVolumnView_A_0, messages.OverSchedualVolumnView_A_1 };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"8", "9", "10", "11", "12" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		double[][] value1 = projectProvider.getDelayValueByYear();

		return BarChart.getChart(xAxisText, bsText, value1, "Stacked", -3); //$NON-NLS-1$
	}

}
