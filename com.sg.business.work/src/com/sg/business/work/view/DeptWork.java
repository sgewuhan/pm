package com.sg.business.work.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.CalendarToolkit;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.PrimaryObjectLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.IRefreshablePart;

public class DeptWork extends ViewPart implements IRefreshablePart {

	private static final int groupcount = 3;
	private GridTreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new GridTreeViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL);
		viewer.getGrid().setHeaderVisible(true);

		createLabelColumn();
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		createGridColumn(currentYear, month);

		viewer.setContentProvider(new DepartmentWorksContentProvider());
		loadData();
	}

	private void createLabelColumn() {
		GridColumn column = new GridColumn(viewer.getGrid(), SWT.NONE);
		column.setWidth(180);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.LEFT);
		// column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS",
		// 16, SWT.NORMAL ) );
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		column.setText("");

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		vColumn.setLabelProvider(new PrimaryObjectLabelProvider());
	}

	/**
	 * 创建某一年的列，展开该年的月份
	 * 
	 * @param year
	 * @param month
	 * @param month
	 */
	private void createGridColumn(int year, int month) {
		for (int i = month-groupcount+1; i < month+1; i++) {
			createMonthGroup(year, i, month == i);
		}
	}
	
	private void createMonthGroup(int year, int month, boolean expand) {
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(),
				SWT.TOGGLE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
//		String yearName = ""+calendar.get(Calendar.YEAR);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, RWT.getLocale());

		group.setText(monthName);

		int days = calendar.getActualMaximum(Calendar.DATE);
		for (int i = 0; i < days; i++) {
			createDayColumn(group, year, month, i);
		}
		createDetailColumn(group, year, month);

		group.setExpanded(!expand);
	}

	private void createDetailColumn(GridColumnGroup group, int year, int month) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(80);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.RIGHT);
		// column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS",
		// 16, SWT.NORMAL ) );
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		column.setDetail(true);
		column.setText("");
		column.setImage(BusinessResource.getImage(BusinessResource.IMAGE_SUMMARY_16));
		
		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		MonthSummaryLabelProvider labelProvider = new MonthSummaryLabelProvider(
				year, month);
		vColumn.setLabelProvider(labelProvider);

	}

	private void createDayColumn(GridColumnGroup group, int year, int month,
			int dateOfMonth) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(36);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.RIGHT);
		// column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS",
		// 16, SWT.NORMAL ) );
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		column.setDetail(false);
		column.setText("" + (dateOfMonth + 1));

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		DateSummaryLabelProvider labelProvider = new DateSummaryLabelProvider(
				year, month, dateOfMonth);
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, dateOfMonth);
		double hours = CalendarToolkit.getWorkingHours(cal.getTime());
		labelProvider.setWorkingHours(hours);
		vColumn.setLabelProvider(labelProvider);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		loadData();
	}

	private void loadData() {
		// 获取当前用户担任管理者角色的部门
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		User user = UserToolkit.getUserById(userId);
		List<PrimaryObject> orglist = user
				.getRoleGrantedInAllOrganization(Role.ROLE_DEPT_MANAGER_ID);
		List<PrimaryObject> input = new ArrayList<PrimaryObject>();

		for (int i = 0; i < orglist.size(); i++) {
			Organization org = (Organization) orglist.get(i);
			boolean hasParent = false;
			for (int j = 0; j < input.size(); j++) {
				Organization inputOrg = (Organization) input.get(j);
				if (inputOrg.isSuperOf(org)) {
					hasParent = true;
					break;
				}
				if (org.isSuperOf(inputOrg)) {
					input.remove(j);
					break;
				}
			}
			if (!hasParent) {
				input.add(org);
			}
		}

		viewer.setInput(input);
	}

}
