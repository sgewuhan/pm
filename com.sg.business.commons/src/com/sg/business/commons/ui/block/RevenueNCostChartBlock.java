package com.sg.business.commons.ui.block;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.commons.ui.chart.CommonChart;
import com.sg.business.model.ProjectProvider;

public class RevenueNCostChartBlock extends AbstractProjectProviderChartBlock {

	public RevenueNCostChartBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected Chart doGetChart() {
		ProjectProvider projectProvider = getProjectProvider();
		String[] bsText = { messages.ProfitVolumnView_1,
				messages.ProfitVolumnView_0 };
		String[] xAxisText = new String[] { "1", "2", "3", "4", "5", "6", "7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"8", "9", "10", "11", "12" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		double[][] value1 = projectProvider.getProfitAndCostByYear(null);

		return CommonChart.getChart(xAxisText, bsText, value1,
				CommonChart.TYPE_BAR, CommonChart.TYPE_SUBTYPE_STACKED, false,
				-5);

	}

}
