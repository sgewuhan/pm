package com.sg.business.home;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sg.widgets.jofc.Chart;
import com.sg.widgets.jofc.facade.JBarChart;
import com.sg.widgets.part.IRefreshablePart;

public class ViewPart1 extends ViewPart implements IRefreshablePart{

	private Chart chart;

	public ViewPart1() {
	}

	@Override
	public void createPartControl(Composite parent) {
		chart = new Chart(parent,SWT.NONE);
		JBarChart chartData = new JBarChart();
		chartData.addBars(new int[]{20,40,40,80,100});
		chart.setChartData(chartData);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		JBarChart chartData = new JBarChart();
		chartData.addBars(new int[]{20,40,40,80,100});
		chart.setChartData(chartData);		
	}

}
