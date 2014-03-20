package com.sg.business.commons.ui.home.basic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;
import com.sg.widgets.block.tab.TabBlock;
import com.sg.widgets.part.IRefreshablePart;

public class BasicHomePanel {

	private Composite panel;
	private int partHeight;

	/**
	 * ����һ���û�����ҳ���
	 * 
	 * ��������Ŀ���������ĵ� ��Ч������
	 * 
	 * @param panel
	 */
	public BasicHomePanel(Composite parent) {
		Rectangle bounds = parent.getDisplay().getBounds();
		partHeight = bounds.height - 61;

		parent.setLayout(new FillLayout());
		panel = new Composite(parent, SWT.NONE);
		panel.setBackground(Widgets.getColor(panel.getDisplay(), 0xed, 0xed,0xed));

		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		panel.setLayout(layout);

		final ProjectBlock projectBlock = new ProjectBlock(panel);
		projectBlock.setTopicText("��Ŀ");
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.heightHint = (Block.TOPICSIZE + 1) + (ProjectBlock.BLOCKSIZE + 1)
				* projectBlock.getCountY() - 1;
		gd.widthHint = (ProjectBlock.BLOCKSIZE + 1) * projectBlock.getCountX() - 1;
		projectBlock.setLayoutData(gd);

		Block workBlock = new TodaysWorkBlock(panel){
			@Override
			public int getContentHeight() {
				return (ProjectBlock.BLOCKSIZE + 1) * projectBlock.getCountY() - 1;
			}
		};
		workBlock.setTopicText("���չ���");
		gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.widthHint = TodaysWorkBlock.BLOCKWIDTH;
//		gd.heightHint = (ProjectBlock.BLOCKSIZE + 1) * ProjectBlock.Y_COUNT - 1;
		workBlock.setLayoutData(gd);

		final DocBlock docBlock = new DocBlock(panel) {
			@Override
			public int getContentHeight() {
				return (ProjectBlock.BLOCKSIZE + 1) * projectBlock.getCountY() - 1;
			}
		};
		docBlock.setTopicText("������ĵ�");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		docBlock.setLayoutData(gd);

		TabBlock performenceBlock = new PerformanceBlock(panel);
		performenceBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 2, 1));

		Block noticeBlock = new BulletinBoardBlock(panel) {
			@Override
			public int getContentHeight() {
				return partHeight - docBlock.getContentHeight()
						- (Block.TOPICSIZE + 1) * 2 - 3;
			}
		};
		noticeBlock.setTopicText("����");
		noticeBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true,
				1, 1));
	}

	public void doRefresh() {
		Control[] children = panel.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof IRefreshablePart) {
				IRefreshablePart refreshablePart = (IRefreshablePart) children[i];
				if (refreshablePart.canRefresh()) {
					refreshablePart.doRefresh();
				}
			}
		}
	}

}
