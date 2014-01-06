package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.chart.CommonChart;
import com.sg.business.visualization.view.AbstractDashChartView;

public class ChangeChartTypeToLineAction extends Action {

	private AbstractDashChartView view;

	public ChangeChartTypeToLineAction(AbstractDashChartView view) {
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
