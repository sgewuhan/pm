package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.commons.ui.chart.CommonChart;
import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.view.AbstractDashChartView;

public class SetChartSubtypeToSidebySideAction extends Action {

	private AbstractDashChartView view;

	public SetChartSubtypeToSidebySideAction(AbstractDashChartView view) {
		setToolTipText("更改数据系列显示为并排显示");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_CHART_SIDEBYSIDE_16));
		this.view = view;
	}

	@Override
	public void run() {
		view.setSubtype(CommonChart.TYPE_SUBTYPE_SIDE_BY_SIDE);
		view.doRefresh();
	}

}
