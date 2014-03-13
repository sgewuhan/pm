package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.commons.ui.chart.CommonChart;
import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.view.AbstractDashChartView;

public class SetChartTypeToLineAction extends Action {

	private AbstractDashChartView view;

	public SetChartTypeToLineAction(AbstractDashChartView view) {
		setToolTipText("œ‘ æŒ™’€œﬂÕº");
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_CHART_LINE_16));
		this.view = view;
	}

	@Override
	public void run() {
		view.setChartType(CommonChart.TYPE_LINE);
		view.doRefresh();
	}

}
