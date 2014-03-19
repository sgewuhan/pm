package com.sg.business.commons.ui.home;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mobnut.db.model.DataSet;
import com.sg.business.commons.ui.block.BudgetNCostChartBlock;
import com.sg.business.commons.ui.block.OverControlChartBlock;
import com.sg.business.commons.ui.block.OverSchdureBlock;
import com.sg.business.commons.ui.block.ProfitRateBlock;
import com.sg.business.commons.ui.block.RevenueNCostChartBlock;
import com.sg.business.model.Organization;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.dataset.organization.OrgOfOwnerManager;
import com.sg.widgets.Widgets;
import com.sg.widgets.part.IRefreshablePart;

public class MonthlyPerformanceHomePanel {

	private Composite panel;
	private ProjectProvider projectProvider;

	/**
	 * 用于展现绩效的主页面板
	 * 
	 * 包括：项目，工作，文档 绩效，公告
	 * 
	 * @param panel
	 */
	public MonthlyPerformanceHomePanel(Composite parent) {
		init();
		parent.setLayout(new FillLayout());
		panel = new Composite(parent, SWT.NONE);
		panel.setBackground(Widgets.getColor(panel.getDisplay(), 0xed, 0xed,0xed));

		GridLayout layout = new GridLayout(2, true);
		layout.horizontalSpacing = 1;
		layout.verticalSpacing = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		panel.setLayout(layout);

		RevenueNCostChartBlock revenueBlock = new RevenueNCostChartBlock(panel);
		revenueBlock.setTopicText("销售利润和成本");
		revenueBlock.setProjectProvider(projectProvider);
		revenueBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		OverControlChartBlock overControlBlock = new OverControlChartBlock(panel);
		overControlBlock.setTopicText("超期和超支的项目");
		overControlBlock.setProjectProvider(projectProvider);
		overControlBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

//		TabBlock performenceBlock = new PerformanceBlock(panel);
//		performenceBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
//				true, 1, 3));

		BudgetNCostChartBlock budgetCostBlock = new BudgetNCostChartBlock(panel);
		budgetCostBlock.setTopicText("预算和累计支出");
		budgetCostBlock.setProjectProvider(projectProvider);
		budgetCostBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		ProfitRateBlock profitRateBlock = new ProfitRateBlock(panel);
		profitRateBlock.setTopicText("平均利润率");
		profitRateBlock.setProjectProvider(projectProvider);
		profitRateBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		OverSchdureBlock overSchedureBlock = new OverSchdureBlock(panel);
		overSchedureBlock.setTopicText("进度正常和超期的项目");
		overSchedureBlock.setProjectProvider(projectProvider);
		overSchedureBlock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		doRefresh();
	}

	private void init() {
		final OrgOfOwnerManager oom = new OrgOfOwnerManager();
		DataSet ds = oom.getDataSet();
		if (!ds.isEmpty()) {
			Organization org = (Organization) ds.getDataItems().get(0);
			projectProvider = org.getAdapter(ProjectProvider.class);
		} else {
			projectProvider = null;
		}		
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
