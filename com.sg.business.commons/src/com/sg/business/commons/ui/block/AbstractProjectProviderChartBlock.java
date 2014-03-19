package com.sg.business.commons.ui.block;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.ProjectProvider;
import com.sg.widgets.birtcharts.ChartCanvas;
import com.sg.widgets.block.Block;

public abstract class AbstractProjectProviderChartBlock extends Block {

	protected static final String PARA_CHART = "chart";
	protected static final String PARA_LABEL = "label";
	protected ChartCanvas graphicContent;
	private ProjectProvider projectProvider;

	public AbstractProjectProviderChartBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void go() {
		
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new FillLayout());
		graphicContent = new ChartCanvas(parent, SWT.NONE, false);
	}

	@Override
	protected void doDisplayData(Object data) {
		if(data instanceof Map){
			Object chart = ((Map<?, ?>) data).get(PARA_CHART);
			if(chart instanceof Chart){
				graphicContent.setChart((Chart) chart);
				try {
					graphicContent.redrawChart(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	final protected Object doGetData() {
		HashMap<String,Object> data = new HashMap<String,Object>();
		Chart chart = doGetChart();
		data.put(PARA_CHART, chart);
		Object label = doGetLabel();
		data.put(PARA_LABEL, label);
		return data;
	}

	protected Object doGetLabel() {
		return null;
	}

	protected abstract Chart doGetChart();
	
	public void setProjectProvider(ProjectProvider projectProvider) {
		this.projectProvider = projectProvider;
	}
	
	protected ProjectProvider getProjectProvider() {
		return projectProvider;
	}
	

}
