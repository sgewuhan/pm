package com.sg.business.visualization.editor;

import org.eclipse.swt.widgets.Composite;

import com.sg.widgets.viewer.CTableViewer;


public class ProjectSetCostTableDetail extends AbstractProjectSetTableDetail {

	@Override
	protected String getTitle() {
		return "项目成本组成明细";
	}


	@Override
	protected Composite createContent(Composite body) {
		Composite content = super.createContent(body);
		CTableViewer tableViewer = (CTableViewer) navi.getViewer();
		
		//创建成本列
		
		
		return content;
	}
}
