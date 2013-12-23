package com.sg.business.visualization.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectETL;
import com.sg.business.resource.BusinessResource;

public class ProfitListView extends AbstractDashWidgetView {

	private int month;
	private int year;
	private int limitNumber;
	private ListCTabItem profitItem;
	private ListCTabItem revenueItem;
	@SuppressWarnings("rawtypes")
	protected Class selected;

	public ProfitListView() {
	}

	@Override
	protected void createContent(Composite parent) {
		Calendar cal = Calendar.getInstance();
		selected = Project.class;
		limitNumber = 10;
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		parent.setLayout(new FillLayout());

		CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));
		tabFolder.setTabHeight(24);

		profitItem = createProfitItem(tabFolder, "������������");
		tabFolder.setSelection(profitItem);

		revenueItem = createProfitItem(tabFolder, "���۶�����");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new RowLayout());

		Button projectButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16));
		projectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProfitListView.this.selected = Project.class;
				doRefresh();
			}
		});

		Button chargerButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_USER_16));
		chargerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProfitListView.this.selected = User.class;
				doRefresh();
			}
		});

		Button orgButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_ORG_16));
		orgButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProfitListView.this.selected = Organization.class;
				doRefresh();
			}
		});
		
		Button settingsButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_SETTINGS_24));
		settingsButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProfitListView.this.selected = User.class;
				doRefresh();
			}
		});
		
		
		tabFolder.setTopRight(composite);

		super.createContent(parent);
	}

	private Button createButton(Composite composite, Image image) {
		Button button = new Button(composite, SWT.PUSH);
		button.setLayoutData(new RowData(20, 20));
		button.setImage(image);
		button.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		return button;
	}

	@Override
	public void applyStyle(Color[] style) {
	}

	private ListCTabItem createProfitItem(CTabFolder tabFolder, String title) {
		ListCTabItem tabItem = new ListCTabItem(tabFolder, title);
		return tabItem;
	}

	@Override
	protected void drawContent(Composite parent) {
		try {
			Object[] topSalesProfit = null;
			Object[] bottomSalesProfit = null;
			Object[] topSales = null;
			Object[] bottomSales = null;
			if (selected.equals(Project.class)) {
				topSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, -1, ProjectETL.F_MONTH_SALES_PROFIT,
						ProjectETL.F_PROJECTID, year, month);
				bottomSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_PROFIT,
						ProjectETL.F_PROJECTID, year, month);

				topSales = projectProvider.getHasLastNumberTopList(limitNumber,
						-1, ProjectETL.F_MONTH_SALES_REVENUE,
						ProjectETL.F_PROJECTID, year, month);
				bottomSales = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_REVENUE,
						ProjectETL.F_PROJECTID, year, month);
			} else if (selected.equals(User.class)) {
				topSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, -1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_CHARGER, year, month);
				bottomSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_CHARGER, year, month);

				topSales = projectProvider.getHasLastNumberTopList(limitNumber,
						-1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_CHARGER, year, month);
				bottomSales = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_CHARGER, year, month);
			} else if (selected.equals(Organization.class)) {
				topSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, -1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month);
				bottomSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month);

				topSales = projectProvider.getHasLastNumberTopList(limitNumber,
						-1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month);
				bottomSales = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month);
			}

			topSalesProfit = removeList(topSalesProfit);
			bottomSalesProfit = removeAndSortList(bottomSalesProfit);
			topSales = removeList(topSales);
			bottomSales = removeAndSortList(bottomSales);

			profitItem.setInput(topSalesProfit, bottomSalesProfit, selected);
			revenueItem.setInput(topSales, bottomSales, selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object[] removeAndSortList(Object[] removeAndSortList) {
		List<Object> asList = Arrays.asList(removeAndSortList);
		List<Object> tempList = new ArrayList<Object>();

		for (Object object : asList) {
			if (object != null) {
				tempList.add(object);
			}
		}
		Comparator<? super Object> sorter = new Comparator<Object>() {
			@Override
			public int compare(Object arg0, Object arg1) {
				if (arg0 == null) {
					return -1;
				}
				if (arg1 == null) {
					return -1;
				}
				Object[] object0 = (Object[]) arg0;
				Object[] object1 = (Object[]) arg1;
				int d0 = (int) object0[2];
				int d1 = (int) object1[2];
				if (d0 > d1) {
					return -1;
				} else if (d0 < d1) {
					return 1;
				} else {
					return 0;
				}
			}

		};
		Collections.sort(tempList, sorter);
		return tempList.toArray(new Object[0]);
	}

	private Object[] removeList(Object[] removeList) {
		List<Object> asList = Arrays.asList(removeList);
		List<Object> tempList = new ArrayList<Object>();

		for (Object object : asList) {
			if (object != null) {
				tempList.add(object);
			}
		}
		return tempList.toArray(new Object[0]);
	}

	@Override
	protected void clean() {
	}

}
