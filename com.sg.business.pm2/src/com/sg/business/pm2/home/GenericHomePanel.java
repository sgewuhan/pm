package com.sg.business.pm2.home;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.pm2.home.widget.ProjectBlock;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;

public class GenericHomePanel {

	/**
	 * 用于一般用户的主页面板
	 * 
	 * 包括：项目，工作，文档
	 * 绩效，公告
	 * 
	 * @param parent
	 */
	public GenericHomePanel(Composite parent) {
		parent.setBackground(Widgets.getColor(parent.getDisplay(),0xed,0xed,0xed));
		GridLayout layout = new GridLayout(3,false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		parent.setLayout(layout);
		
		Block projectBlock = new ProjectBlock(parent);
		projectBlock.setTopicText("项目");
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.heightHint = (Block.TOPICSIZE+1)*6;
		gd.widthHint = (Block.TOPICSIZE+1)*5;
		projectBlock.setLayoutData(gd);
		
		Block workBlock = new Block(parent);
		workBlock.setTopicText("工作");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		workBlock.setLayoutData(gd);
		
		Block docBlock = new Block(parent);
		docBlock.setTopicText("文档");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		docBlock.setLayoutData(gd);

		Block performenceBlock = new Block(parent);
		performenceBlock.setTopicText("绩效");
		performenceBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));

		Block noticeBlock = new Block(parent);
		noticeBlock.setTopicText("公告");
		noticeBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	}

}
