package com.sg.business.visualization.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ProjectSetContent extends AbstractProjectPage {

	public ProjectSetContent() {

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

}
