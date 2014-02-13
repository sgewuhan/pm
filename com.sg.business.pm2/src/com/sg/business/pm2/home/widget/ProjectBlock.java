package com.sg.business.pm2.home.widget;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.widgets.block.Block;

public class ProjectBlock extends Block {

	public ProjectBlock(Composite parent) {
		super(parent);
	}

	@Override
	protected void createContent(Composite parent) {
		parent.setLayout(new FillLayout());
		//显示我负责的项目
		
		
	}
}
