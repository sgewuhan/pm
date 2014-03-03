package com.sg.business.pm2.home.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.sg.business.pm2.home.page.BudgetPage;
import com.sg.business.pm2.home.page.RevenuePage;
import com.sg.business.pm2.home.page.SchedulePage;
import com.sg.widgets.block.TabBlock;

public class PerformanceBlock extends TabBlock {

	private static final int ID_LISTBOARD = 0;
	private static final int ID_REVENUE = 1;
	private static final int ID_BUDGET = 2;
	private static final int ID_SCHEDULE = 3;
	private SchedulePage schedulePage;
	private BudgetPage budgetPage;
	private RevenuePage revenuePage;

	public PerformanceBlock(Composite parent) {
		super(parent, SWT.NONE);

		addPage(ID_SCHEDULE, "进度指标", null);
		addPage(ID_BUDGET, "预算控制", null);
		addPage(ID_REVENUE, "经济效益", null);
		addPage(ID_LISTBOARD, "排行榜", null);

		select(ID_SCHEDULE);
	}

	@Override
	protected Control createPage(int pageId, Composite parent) {
		switch (pageId) {
		case ID_LISTBOARD:
			return createListBoardPage(parent);
		case ID_BUDGET:
			return createBudgetPage(parent);
		case ID_SCHEDULE:
			return createSchedulePage(parent);
		case ID_REVENUE:
			return createRevenuePage(parent);
		default:
			return null;
		}
	}
	
	@Override
	protected void pageRefresh(int pageId, Control page) {
		switch (pageId) {
		case ID_LISTBOARD:
			refreshListBoardPage();
		case ID_BUDGET:
			refreshBudgetPage();
		case ID_SCHEDULE:
			refreshSchedulePage();
		case ID_REVENUE:
			refreshRevenuePage();
		default:
		}
	}

	private void refreshRevenuePage() {
		if(revenuePage!=null&&revenuePage.canRefresh()){
			revenuePage.doRefresh();
		}
	}

	private void refreshSchedulePage() {
		if(schedulePage!=null&&schedulePage.canRefresh()){
			schedulePage.doRefresh();		
		}		
	}

	private void refreshBudgetPage() {
		if(budgetPage!=null&&budgetPage.canRefresh()){
			budgetPage.doRefresh();		
		}		
	}

	private void refreshListBoardPage() {
	}

	private Control createRevenuePage(Composite parent) {
		revenuePage =  new RevenuePage(parent);
		return revenuePage;
	}

	private Control createSchedulePage(Composite parent) {
		schedulePage =  new SchedulePage(parent);
		return schedulePage;
	}

	private Control createBudgetPage(Composite parent) {
		budgetPage =  new BudgetPage(parent);
		return budgetPage;
	}

	private Control createListBoardPage(Composite parent) {
		Label control = new Label(parent, SWT.NONE);
		control.setText("List");
		return control;
	}


}
