package com.sg.business.commons.ui.home.perf;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.sg.widgets.block.tab.TabBlock;

public class ManagementPerformanceBlock extends TabBlock {

	private static final int ID_CHARGERLISTBOARD = 1;
	private static final int ID_ORGANIZATIONLISTBOARD = 0;
	private static final int ID_PROJECTLISTBOARD = 2;
	private ChargerListBoardPage chargetListBoardPage;
	private OrganizationListBoardPage organizationListBoardPage;
	private ProjectListBoardPage projectListBoardPage;

	public ManagementPerformanceBlock(Composite parent) {
		super(parent, SWT.NONE);

		addPage(ID_ORGANIZATIONLISTBOARD, "组织", null);
		addPage(ID_PROJECTLISTBOARD, "项目", null);
		addPage(ID_CHARGERLISTBOARD, "项目经理", null);

		select(ID_ORGANIZATIONLISTBOARD);
	}

	@Override
	protected Control createPage(int pageId, Composite parent) {
		switch (pageId) {
		case ID_CHARGERLISTBOARD:
			return createChargerListBoardPage(parent);
		case ID_ORGANIZATIONLISTBOARD:
			return createOrganizationListBoardPage(parent);
		case ID_PROJECTLISTBOARD:
			return createProjectListBoardPage(parent);
		default:
			return null;
		}
	}

	@Override
	protected void pageRefresh(int pageId, Control page) {
		switch (pageId) {
		case ID_CHARGERLISTBOARD:
			refreshChargerListBoardPage();
			break;
		case ID_ORGANIZATIONLISTBOARD:
			refreshOrganizationListBoardPage();
			break;
		case ID_PROJECTLISTBOARD:
			refreshProjectListBoardPage();
			break;
		default:
		}
	}

	private void refreshProjectListBoardPage() {
		if (projectListBoardPage.canRefresh()) {
			projectListBoardPage.doRefresh();
		}
	}

	private void refreshOrganizationListBoardPage() {
		if (organizationListBoardPage.canRefresh()) {
			organizationListBoardPage.doRefresh();
		}
	}

	private void refreshChargerListBoardPage() {
		if (chargetListBoardPage.canRefresh()) {
			chargetListBoardPage.doRefresh();
		}
	}

	private Control createProjectListBoardPage(Composite parent) {
		projectListBoardPage = new ProjectListBoardPage(parent);
		return projectListBoardPage;
	}

	private Control createOrganizationListBoardPage(Composite parent) {
		organizationListBoardPage = new OrganizationListBoardPage(parent);
		return organizationListBoardPage;
	}

	private Control createChargerListBoardPage(Composite parent) {
		chargetListBoardPage = new ChargerListBoardPage(parent);
		return chargetListBoardPage;
	}

}
