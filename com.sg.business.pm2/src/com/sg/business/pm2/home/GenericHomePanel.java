package com.sg.business.pm2.home;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.pm2.home.widget.ProjectBlock;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;

public class GenericHomePanel {

	/**
	 * ����һ���û�����ҳ���
	 * 
	 * ��������Ŀ���������ĵ�
	 * ��Ч������
	 * 
	 * @param panel
	 */
	public GenericHomePanel(Composite parent) {
		parent.setLayout(new FillLayout());
		Composite panel = new Composite(parent,SWT.NONE);
		panel.setBackground(Widgets.getColor(panel.getDisplay(),0xed,0xed,0xed));
		GridLayout layout = new GridLayout(3,false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		panel.setLayout(layout);
		
		Block projectBlock = new ProjectBlock(panel);
		projectBlock.setTopicText("��Ŀ");
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.heightHint = (Block.TOPICSIZE+1)*6;
		gd.widthHint = (Block.TOPICSIZE+1)*5;
		projectBlock.setLayoutData(gd);
		
		Block workBlock = new Block(panel);
		workBlock.setTopicText("����");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		workBlock.setLayoutData(gd);
		
		Block docBlock = new Block(panel);
		docBlock.setTopicText("�ĵ�");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		docBlock.setLayoutData(gd);

		Block performenceBlock = new Block(panel);
		performenceBlock.setTopicText("��Ч");
		performenceBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));

		Block noticeBlock = new Block(panel);
		noticeBlock.setTopicText("����");
		noticeBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

	}

}