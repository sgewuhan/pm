package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.widgets.block.tab.TabBlock;

public class OverBudgetAndDelayTop10Block extends TabBlock {

	private static final int ID_OVERBUDGET = 0;
	private static final int ID_DELAY = 1;
	private OverBudgetTop10Page overBudgetPage;
	private DelayTop10Page delayPage;

	public OverBudgetAndDelayTop10Block(Composite parent) {
		super(parent, SWT.NONE);
		addPage(ID_OVERBUDGET, "³¬Ö§", null);
		addPage(ID_DELAY, "³¬ÆÚ", null);
		select(ID_OVERBUDGET);
	}

	@Override
	protected Control createPage(int pageId, Composite parent) {
		switch (pageId) {
		case ID_OVERBUDGET:
			return createOverBudgetPage(parent);
		case ID_DELAY:
			return createDelayPage(parent);
		default:
			return null;
		}
	}
	
	@Override
	protected void pageRefresh(int pageId, Control page) {
		switch (pageId) {
		case ID_OVERBUDGET:
			refreshOverBudgetPage();
		case ID_DELAY:
			refreshDelayPage();
		default:
		}
	}

	private void refreshDelayPage() {
		if(delayPage!=null&&delayPage.canRefresh()){
			delayPage.doRefresh();
		}
	}

	private void refreshOverBudgetPage() {
		if(overBudgetPage.canRefresh()){
			overBudgetPage.doRefresh();
		}
	}

	private Control createDelayPage(Composite parent) {
		delayPage =  new DelayTop10Page(parent);
		return delayPage;
	}

	private Control createOverBudgetPage(Composite parent) {
		overBudgetPage = new OverBudgetTop10Page(parent);
		return overBudgetPage;
	}


}
