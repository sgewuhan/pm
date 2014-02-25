package com.sg.business.pm2.home.page;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.widgets.block.TabBlockPage;

public class SchedulePage extends TabBlockPage {

	public SchedulePage(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		Control grsphic = createGraphicBlock(this);
		FormData fd = new FormData();
		grsphic.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(50);
		fd.bottom = new FormAttachment(100);
		
		Control text = createTextBlock(this);
		fd = new FormData();
		text.setLayoutData(fd);
		fd.top = new FormAttachment();
		fd.left = new FormAttachment(grsphic);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);
	}

	private Control createTextBlock(Composite parent) {
		Composite control = new Composite(parent,SWT.NONE);
		control.setBackground(new Color(getDisplay(),255,0,0));
		return control;
	}

	private Control createGraphicBlock(Composite parent) {
		Composite control = new Composite(parent,SWT.NONE);
		control.setBackground(new Color(getDisplay(),255,255,0));
		return control;
		
	}

}
