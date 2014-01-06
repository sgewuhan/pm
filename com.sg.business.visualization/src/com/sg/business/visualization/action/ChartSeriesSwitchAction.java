package com.sg.business.visualization.action;

import org.eclipse.jface.action.Action;

import com.sg.business.resource.BusinessResource;
import com.sg.business.visualization.view.AbstractDashChartView;

public class ChartSeriesSwitchAction extends Action {
	private AbstractDashChartView view;

	public ChartSeriesSwitchAction(AbstractDashChartView view) {
		setToolTipText("��ʾ����ϵ�б�ǩ");
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
