package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.view.AbstractDashChartView;

public class ChartSeriesSwitchAction extends Action {
	private AbstractDashChartView view;

	public ChartSeriesSwitchAction(AbstractDashChartView view) {
		setToolTipText("显示数据系列标签");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_CHART_SHOW_NUMBER_16));
		this.view = view;
	}

	@Override
	public void run() {
		view.switchSeriesLabel();
		view.doRefresh();
	}
}
