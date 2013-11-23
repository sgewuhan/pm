package com.sg.business.visualization.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ProjectSetDashboardSchedual extends AbstractProjectPage {

	@Override
	protected Composite createContent(Composite body) {
		SashForm content= new SashForm(body, SWT.HORIZONTAL);
		Composite tableContent = new Composite(content,SWT.NONE);
		navi.createPartContent(tableContent);
		
		Composite graphicContent = new Composite(content,SWT.NONE);
		graphicContent .setLayout(new GridLayout());
		createPieGraphic(graphicContent);
		
		content.setWeights(new int[]{3,1});
		return content;
	}

	private void createPieGraphic(Composite parent) {
		//显示总数，已经完成，进行中已经延期，可能延期
		
		
	}

	

}
