package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.widgets.block.tab.TabBlock;

public class ManagementPerformanceBlock extends TabBlock {

	private static final int ID_CHARGERLISTBOARD = 0;
	private ChargerListBoardPage chargetListBoardPage;

	public ManagementPerformanceBlock(Composite parent) {
		super(parent, SWT.NONE);

		addPage(ID_CHARGERLISTBOARD, "项目负责人", null);

		select(ID_CHARGERLISTBOARD);
	}

	@Override
	protected Control createPage(int pageId, Composite parent) {
		switch (pageId) {
		case ID_CHARGERLISTBOARD:
			return createChargerListBoardPage(parent);
		default:
			return null;
		}
	}

	@Override
	protected void pageRefresh(int pageId, Control page) {
		switch (pageId) {
		case ID_CHARGERLISTBOARD:
			refreshChargerListBoardPage();
		default:
		}
	}

	private void refreshChargerListBoardPage() {
		if (chargetListBoardPage.canRefresh()) {
			chargetListBoardPage.doRefresh();
		}
	}

	private Control createChargerListBoardPage(Composite parent) {
		chargetListBoardPage = new ChargerListBoardPage(parent);
		return chargetListBoardPage;
	}

}
