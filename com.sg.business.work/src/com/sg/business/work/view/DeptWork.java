package com.sg.business.work.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
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
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		for (int i = month - groupcount + 1; i < month + 1; i++) {
			createMonthGroup(year, i, month == i);
		}

		viewer.setContentProvider(new DepartmentWorksContentProvider());
		createGridViewerEditor();

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


	private void createMonthGroup(int year, int month, boolean expand) {
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(),
				SWT.TOGGLE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		// String yearName = ""+calendar.get(Calendar.YEAR);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, RWT.getLocale());

		group.setText(monthName);

		int days = calendar.getActualMaximum(Calendar.DATE);
		for (int i = 0; i < days; i++) {
			createDayColumn(group, year, month, i+1);
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
		column.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_SUMMARY_16));

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);
		MonthSummaryLabelProvider labelProvider = new MonthSummaryLabelProvider(
				year, month);
		vColumn.setLabelProvider(labelProvider);

	}
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 9);
		cal.set(Calendar.DATE, 16);
//		cal.set(Calendar.HOUR, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
		
		System.out.println(cal.getTimeInMillis()/(24*60*60*1000));
		
	}

	private void createDayColumn(GridColumnGroup group, final int year,
			final int month, final int dateOfMonth) {
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
		column.setText("" + dateOfMonth);

		GridViewerColumn vColumn = new GridViewerColumn(viewer, column);

		
		final DateSummaryLabelProvider labelProvider = new DateSummaryLabelProvider(
				year, month, dateOfMonth);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, dateOfMonth);

		double hours = CalendarToolkit.getWorkingHours(cal.getTime());
		labelProvider.setWorkingHours(hours);
		vColumn.setLabelProvider(labelProvider);

		vColumn.setEditingSupport(new EditingSupport(viewer) {

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new WorkListCellEditor(viewer.getGrid(), (User) element,
						year, month, dateOfMonth);
			}

			@Override
			protected boolean canEdit(Object element) {
				return element instanceof User;
			}

			@Override
			protected Object getValue(Object element) {
				return "";
			}

			@Override
			protected void setValue(Object element, Object value) {
			}

		});
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

	private void createGridViewerEditor() {
		ColumnViewerEditorActivationStrategy strategy = new ColumnViewerEditorActivationStrategy(
				viewer) {
			@Override
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				int type = event.eventType;
				int keyCode = event.keyCode;
				return type == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| type == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
						|| (type == ColumnViewerEditorActivationEvent.KEY_PRESSED && keyCode == SWT.CR)
						|| type == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
		int feature = ColumnViewerEditor.TABBING_HORIZONTAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION;
		GridViewerEditor.create(viewer, strategy, feature);
	}
}
