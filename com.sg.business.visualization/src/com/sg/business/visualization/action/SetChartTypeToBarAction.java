package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.commons.ui.chart.CommonChart;
import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.view.AbstractDashChartView;

public class SetChartTypeToBarAction extends Action {

	private AbstractDashChartView view;

	public SetChartTypeToBarAction(AbstractDashChartView view) {
		setToolTipText("��ʾΪ����ͼ");
		setImageDescriptor(BusinessResource.getImageDescriptor(BusinessResource.IMAGE_CHART_BAR_16));
		this.view = view;
	}

	@Override
	public void run() {
		view.setChartType(CommonChart.TYPE_BAR);
		view.doRefresh();
	}

}
