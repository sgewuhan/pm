package com.sg.sales.ui.block;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.business.commons.ui.block.SchedulePage;
import com.sg.widgets.block.tab.TabBlock;
import com.sg.widgets.block.tab.TabBlockPage;

public class SalesPerformanceBlock extends TabBlock {

	private static final int ID_BASIC_PERFORMENCE = 0;
	private static final int ID_SCHEDULE = 1;
	private static final int ID_CONTRACT_PERFORMENCE = 2;
	private static final int ID_INCOME_PERFORMENCE = 3;
	private TabBlockPage schedulePage;
	private TabBlockPage basicPage;
	private TabBlockPage contractPage;
	private TabBlockPage incomePage;

	public SalesPerformanceBlock(Composite parent) {
		super(parent, SWT.NONE);

		addPage(ID_BASIC_PERFORMENCE, "基本业绩", null);
		addPage(ID_CONTRACT_PERFORMENCE, "签约合同", null);
		addPage(ID_INCOME_PERFORMENCE, "回款状况", null);
		addPage(ID_SCHEDULE, "工作进度", null);
		select(ID_BASIC_PERFORMENCE);
	}

	@Override
	protected Control createPage(int pageId, Composite parent) {
		switch (pageId) {
		case ID_BASIC_PERFORMENCE:
			return createSalesPerformencePage(parent);
		case ID_SCHEDULE:
			return createSchedulePage(parent);
		case ID_CONTRACT_PERFORMENCE:
			return createContractPage(parent);
		case ID_INCOME_PERFORMENCE:
			return createIncomePage(parent);
		default:
			return null;
		}
	}
	

	@Override
	protected void pageRefresh(int pageId, Control page) {
		switch (pageId) {
		case ID_BASIC_PERFORMENCE:
			refreshBasicPage();
		case ID_SCHEDULE:
			refreshSchedulePage();
		case ID_CONTRACT_PERFORMENCE:
			refreshContractPage();
		case ID_INCOME_PERFORMENCE:
			refreshIncomePage();
		default:
		}
	}


	private Control createSalesPerformencePage(Composite parent) {
		basicPage =  new SalesBasicPerformencePage(parent);
		return basicPage;
	}
	private void refreshBasicPage() {
		if(basicPage!=null&&basicPage.canRefresh()){
			basicPage.doRefresh();		
		}		
	}

	private void refreshSchedulePage() {
		if(schedulePage!=null&&schedulePage.canRefresh()){
			schedulePage.doRefresh();		
		}		
	}

	private Control createSchedulePage(Composite parent) {
		schedulePage =  new SchedulePage(parent);
		return schedulePage;
	}

	private void refreshContractPage() {
		if(contractPage!=null&&contractPage.canRefresh()){
			contractPage.doRefresh();		
		}		
	}

	private Control createContractPage(Composite parent) {
		contractPage =  new ContractPage(parent);
		return contractPage;
	}

	private void refreshIncomePage() {
		if(incomePage!=null&&incomePage.canRefresh()){
			incomePage.doRefresh();		
		}		
	}

	private Control createIncomePage(Composite parent) {
		incomePage =  new IncomePage(parent);
		return incomePage;
	}



}
