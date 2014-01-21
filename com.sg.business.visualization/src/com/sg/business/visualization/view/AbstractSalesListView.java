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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.business.visualization.ui.SalesListSetting;

public abstract class AbstractSalesListView extends AbstractDashWidgetView {

	protected int month;
	protected int year;
	protected int limitNumber;
	protected ListCTabItem profitItem;
	protected ListCTabItem revenueItem;
	@SuppressWarnings("rawtypes")
	protected Class selected;

	public AbstractSalesListView() {
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setLimitNumber(int limitNumber) {
		this.limitNumber = limitNumber;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public int getLimitNumber() {
		return limitNumber;
	}

	@Override
	protected void createContent(Composite parent) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		selected = Project.class;
		limitNumber = 10;
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH) + 1;
		parent.setLayout(new FillLayout());

		final CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
		tabFolder.setData(RWT.CUSTOM_VARIANT, "toplist"); //$NON-NLS-1$
		// tabFolder.setBackground(parent.getDisplay().getSystemColor(
		// SWT.COLOR_WHITE));
		tabFolder.setTabHeight(24);

		profitItem = createProfitItem(tabFolder, Messages.get().SalesListView_1);
		// profitItem.setData(RWT.CUSTOM_VARIANT, "toplist");
		tabFolder.setSelection(profitItem);

		revenueItem = createProfitItem(tabFolder,
				Messages.get().SalesListView_2);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new RowLayout());

		Button projectButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_PROJECT_16));
		projectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AbstractSalesListView.this.selected = Project.class;
				doRefresh();
			}
		});

		Button chargerButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_USER_16));
		chargerButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AbstractSalesListView.this.selected = User.class;
				doRefresh();
			}
		});

		Button orgButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_ORG_16));
		orgButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AbstractSalesListView.this.selected = Organization.class;
				doRefresh();
			}
		});

		Button settingsButton = createButton(composite,
				BusinessResource.getImage(BusinessResource.IMAGE_SETTINGS_24));

		settingsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SalesListSetting shell = new SalesListSetting(tabFolder
						.getShell(), AbstractSalesListView.this) {
					@Override
					protected void setFilter(int yearIndex, int monthIndex,
							int limitNumberIndex) {
						super.setFilter(yearIndex, monthIndex, limitNumberIndex);
						doRefresh();
					}
				};

				shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Point location = new Point(0, 100);
				shell.open(location);

			}
		});

		tabFolder.setTopRight(composite);

		super.createContent(parent);
	}

	private Button createButton(Composite composite, Image image) {
		Button button = new Button(composite, SWT.PUSH);
		button.setLayoutData(new RowData(20, 20));
		button.setImage(image);
		button.setData(RWT.CUSTOM_VARIANT, "whitebutton"); //$NON-NLS-1$
		return button;
	}

	// @Override
	// public void applyStyle(Color[] style) {
	// }

	private ListCTabItem createProfitItem(CTabFolder tabFolder, String title) {
		ListCTabItem tabItem = new ListCTabItem(tabFolder, title);
		return tabItem;
	}

	protected Object[] removeAndSortList(Object[] removeAndSortList) {
		List<Object> asList = Arrays.asList(removeAndSortList);
		List<Object> tempList = new ArrayList<Object>();

		for (Object object : asList) {
			if (object != null) {
				Object[] children = (Object[]) object;
				List<Object> asChildrenList = Arrays.asList(children);
				List<Object> tempChildrenList = new ArrayList<Object>();
				tempChildrenList.addAll(asChildrenList);
				tempChildrenList.add(Boolean.FALSE);
				tempList.add(tempChildrenList.toArray(new Object[0]));
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

	protected Object[] removeList(Object[] removeList) {
		List<Object> asList = Arrays.asList(removeList);
		List<Object> tempList = new ArrayList<Object>();

		for (Object object : asList) {
			if (object != null) {
				Object[] children = (Object[]) object;
				List<Object> asChildrenList = Arrays.asList(children);
				List<Object> tempChildrenList = new ArrayList<Object>();
				tempChildrenList.addAll(asChildrenList);
				tempChildrenList.add(Boolean.TRUE);
				tempList.add(tempChildrenList.toArray(new Object[0]));
			}
		}
		return tempList.toArray(new Object[0]);
	}

	@Override
	protected void clean() {
	}
}
