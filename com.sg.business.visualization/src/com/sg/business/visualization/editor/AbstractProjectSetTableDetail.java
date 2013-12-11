package com.sg.business.visualization.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractProjectSetTableDetail extends AbstractProjectPage {

	
	public AbstractProjectSetTableDetail() {

	}

	@Override
	protected Composite createContent(Composite body) {
		Composite content = new Composite(body, SWT.NONE);
		navi.createPartContent(content);
		return content;
	}
	

	protected boolean displaySummary() {
		return false;
	}


	@Override
	protected String getProjectSetPageLabel() {
		String projectSetName = data.getProjectSetName();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:13pt'>");
		sb.append(projectSetName + " "+getTitle());
		sb.append("</span>");
		return sb.toString();
	}

	protected abstract String getTitle();
	
}
