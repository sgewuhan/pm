package com.sg.sales.ui.home;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.ui.block.BulletinBoardBlock;
import com.sg.business.commons.ui.block.TodaysWorkBlock;
import com.sg.sales.ui.block.CompanyBlock;
import com.sg.sales.ui.block.OpportunityBlock;
import com.sg.sales.ui.block.SalesPerformanceBlock;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;
import com.sg.widgets.block.tab.TabBlock;
import com.sg.widgets.part.IRefreshablePart;

public class SalesHomePanel {

	private Composite panel;
	private int partHeight;

	/**
	 * 用于一般用户的主页面板
	 * 
	 * 包括：项目，工作，文档 绩效，公告
	 * 
	 * @param panel
	 */
	public SalesHomePanel(Composite parent) {
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

		Block companyBlock = new CompanyBlock(panel);
		companyBlock.setTopicText("客户");
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.heightHint = (Block.TOPICSIZE + 1) + (CompanyBlock.BLOCKSIZE + 1)
				* CompanyBlock.Y_COUNT - 1;
		gd.widthHint = (CompanyBlock.BLOCKSIZE + 1) * CompanyBlock.X_COUNT - 1;
		companyBlock.setLayoutData(gd);

		Block workBlock = new TodaysWorkBlock(panel){
			@Override
			public int getContentHeight() {
				return (CompanyBlock.BLOCKSIZE + 1) * CompanyBlock.Y_COUNT - 1;
			}
		};
		workBlock.setTopicText("今日工作");
		gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.widthHint = TodaysWorkBlock.BLOCKWIDTH;
//		gd.heightHint = (ButtonBlock.BLOCKSIZE + 1) * ButtonBlock.Y_COUNT - 1;
		workBlock.setLayoutData(gd);

		final OpportunityBlock block = new OpportunityBlock(panel) {
			@Override
			public int getContentHeight() {
				return (CompanyBlock.BLOCKSIZE + 1) * CompanyBlock.Y_COUNT - 1;
			}
		};
		block.setTopicText("商机");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		block.setLayoutData(gd);

		TabBlock performenceBlock = new SalesPerformanceBlock(panel);
		performenceBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 2, 1));

		Block noticeBlock = new BulletinBoardBlock(panel) {
			@Override
			protected int getContentHeight() {
				return partHeight - block.getContentHeight()
						- (Block.TOPICSIZE + 1) * 2 - 3;
			}
		};
		noticeBlock.setTopicText("公告");
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
