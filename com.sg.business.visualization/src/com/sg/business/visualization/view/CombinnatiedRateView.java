package com.sg.business.visualization.view;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.widgets.birtcharts.ChartCanvas;

public class CombinnatiedRateView extends AbstractDashWidgetView {

	private ChartCanvas chart;

	public CombinnatiedRateView() {
	}

	@Override
	protected void drawContent(Composite parent) {
		parent.setLayout(new FillLayout());
		
		chart= new ChartCanvas(parent, SWT.NONE){
			@Override
			public Chart getChart() {
				
				return CombinnatiedRateView.this.getChart();
			}
		};
		
		layout(chart, 1, 1);
	}

	private Chart getChart() {
		
		
		return null;
	}
	
	

}
