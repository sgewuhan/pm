package com.sg.business.commons.ui.block;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.ProjectProvider;

public abstract class AbstractProjectProviderChartBlock extends AbstractChartBlock {

	private ProjectProvider projectProvider;

	public AbstractProjectProviderChartBlock(Composite parent) {
		super(parent);
	}

	protected abstract Chart doGetChart();
	
	public void setProjectProvider(ProjectProvider projectProvider) {
		this.projectProvider = projectProvider;
	}
	
	protected ProjectProvider getProjectProvider() {
		return projectProvider;
	}
	

}
