package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.chart.CommonChart;
import com.sg.business.visualization.view.AbstractDashChartView;

public class SetChartSubtypeToStackedAction extends Action {

	private AbstractDashChartView view;

	public SetChartSubtypeToStackedAction(AbstractDashChartView view) {
		setToolTipText("更改数据系列显示为堆叠显示");
		setImageDescriptor(BusinessResource
				.getImageDescriptor(BusinessResource.IMAGE_CHART_STACKED_16));
		this.view = view;
	}

	@Override
	public void run() {
		view.setSubtype(CommonChart.TYPE_SUBTYPE_STACKED);
		view.doRefresh();
	}

}
