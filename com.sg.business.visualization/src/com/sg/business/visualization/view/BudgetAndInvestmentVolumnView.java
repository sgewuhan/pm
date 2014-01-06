package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;

import com.sg.business.visualization.chart.CommonChart;
import com.sg.business.visualization.nls.Messages;

public class BudgetAndInvestmentVolumnView extends AbstractDashChartView {

	@Override
	protected Chart getChartData() throws Exception {
		Messages messages = Messages.get(locale);
		String[] bsText = { messages.BudgetAndInvestmentVolumnView_A_0,
				messages.BudgetAndInvestmentVolumnView_A_1 };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"8", "9", "10", "11", "12" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		double[] value1 = projectProvider
				.getValueByYear(new String[] { "budget" }); //$NON-NLS-1$
		double[] value2 = projectProvider.getValueByYear(new String[] {
				"investment_designated", "investment_allocated" }); //$NON-NLS-1$ //$NON-NLS-2$

		return CommonChart.getChart(xAxisText, bsText, new double[][] { value1,
				value2 }, CommonChart.TYPE_BAR,CommonChart.TYPE_SUBTYPE_SIDE_BY_SIDE,showSeriesLabel, -1); //$NON-NLS-1$

	}
}
