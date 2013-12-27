package com.sg.business.visualization.editor.projectset;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SummaryBlock extends Composite{

	private Label label;

	public SummaryBlock(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		label = new Label(this,SWT.NONE);
		label.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
	}
	
	public void setText(String html){
		label.setText(html);
	}

}
