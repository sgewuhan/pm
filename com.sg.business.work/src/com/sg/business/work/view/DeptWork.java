package com.sg.business.work.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bson.types.ObjectId;
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
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.commons.labelprovider.PrimaryObjectLabelProvider;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.IRefreshablePart;

public class DeptWork extends ViewPart implements IRefreshablePart {

	private GridTreeViewer viewer;
	private int currentYear;
	private HashMap<ObjectId,PrimaryObject> cache;

	@Override
	public void createPartControl(Composite parent) {
		cache = new HashMap<ObjectId,PrimaryObject>();
		
		viewer = new GridTreeViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL);
		viewer.getGrid().setHeaderVisible(true);

		createLabelColumn();
		Calendar calendar = Calendar.getInstance();
		currentYear = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		createGridColumn(month);

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
	 */
	private void createGridColumn(int month) {

		for (int i = 0; i < 12; i++) {
			createMonthGroup(i, month == i);
		}

	}

	private void createMonthGroup(int month, boolean expand) {
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(),
				SWT.TOGGLE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, RWT.getLocale());
		group.setText(monthName);

		int days = calendar.getActualMaximum(Calendar.DATE);
		for (int i = 0; i < days; i++) {
			createDayColumn(group,month, i);
		}
		createDetailColumn(group, month);

		group.setExpanded(!expand);
	}

	private void createDetailColumn(GridColumnGroup group, int month) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(80);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.CENTER);
		// column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS",
		// 16, SWT.NORMAL ) );
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		column.setDetail(true);
		column.setText("");
		
		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		MonthSummaryLabelProvider labelProvider = new MonthSummaryLabelProvider(currentYear,month);
		labelProvider.setCache(cache);
		vColumn.setLabelProvider(labelProvider);
		
	}

	private void createDayColumn(GridColumnGroup group, int month, int dateOfMonth) {
		GridColumn column = new GridColumn(group, SWT.NONE);
		column.setWidth(40);
		// column.setImage( image );
		// column.setSort( SWT.DOWN );
		// column.setFooterImage( image );
		column.setAlignment(SWT.CENTER);
		// column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS",
		// 16, SWT.NORMAL ) );
		// column.setFooterFont( new Font( column.getDisplay(), "Segoe Script",
		// 16, SWT.NORMAL ) );
		// column.setSummary( false );
		// column.setMinimumWidth( 100 );
		column.setDetail(false);
		column.setText("" + (dateOfMonth + 1));

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		DateSummaryLabelProvider labelProvider = new DateSummaryLabelProvider(currentYear,month,dateOfMonth);
		labelProvider.setCache(cache);
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
		
		//创建缓存
		cache .clear();
		
		viewer.setInput(input);
	}

}
