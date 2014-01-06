package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;

import com.sg.business.visualization.chart.CommonChart;
import com.sg.business.visualization.nls.Messages;

public class CombinnatiedRateView extends AbstractDashChartView {

	@Override
	protected Chart getChartData() throws Exception {
		Messages messages = Messages.get(locale);
		String[] lsText = { messages.CombinnatiedRateView_A_0,
				messages.CombinnatiedRateView_A_1 };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"8", "9", "10", "11", "12" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		double[] value1 = projectProvider.getRateValueByYear("overcost_def"); //$NON-NLS-1$
		double[] value2 = projectProvider.getRateValueByYear("isdelay_def"); //$NON-NLS-1$

		return CommonChart.getChart(xAxisText, lsText, new double[][] { value1,
				value2 }, CommonChart.TYPE_LINE,
				CommonChart.TYPE_SUBTYPE_OVERLAY, showSeriesLabel, -2);
	}

}
