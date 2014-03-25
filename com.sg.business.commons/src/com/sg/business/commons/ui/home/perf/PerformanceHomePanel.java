package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.ui.home.basic.PerformanceBlock;
import com.sg.widgets.Widgets;
import com.sg.widgets.block.Block;
import com.sg.widgets.block.tab.TabBlock;
import com.sg.widgets.part.IRefreshablePart;

public class PerformanceHomePanel {

	private Composite panel;

	/**
	 * 用于管理者的主页面板
	 * 
	 * 包括：重点关注，超支/超期 top10, 销售利润收入 top10
	 * 
	 * @param panel
	 */
	public PerformanceHomePanel(Composite parent) {
		parent.setLayout(new FillLayout());
		panel = new Composite(parent, SWT.NONE);
		panel.setBackground(Widgets.getColor(panel.getDisplay(), 0xed, 0xed,
				0xed));

		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		panel.setLayout(layout);

		ImportantProjectBlock vipBlock = new ImportantProjectBlock(panel);
		vipBlock.setTopicText("关注点");
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd.heightHint = (Block.TOPICSIZE + 1)
				+ (ImportantProjectBlock.BLOCKSIZE + 1) * vipBlock.getUnitCountY()
				- 1;
		gd.widthHint = (ImportantProjectBlock.BLOCKSIZE + 1)
				* vipBlock.getUnitCountX() - 1;
		vipBlock.setLayoutData(gd);

		Composite performenceBlock = new OverBudgetAndDelayTop10Block2(panel);
		performenceBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 2));

		TabBlock performenceBlock2 = new PerformanceBlock(panel);
		performenceBlock2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true, 1, 1));

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
