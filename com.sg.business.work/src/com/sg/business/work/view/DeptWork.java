package com.sg.business.work.view;

import java.util.Calendar;

import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sg.widgets.part.IRefreshablePart;

public class DeptWork extends ViewPart implements IRefreshablePart {

	private GridTreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new GridTreeViewer(parent, SWT.FULL_SELECTION|SWT.H_SCROLL);
		viewer.getGrid().setHeaderVisible(true);
		
		createLabelColumn();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		createGridColumn(year, month);
	}

	private void createLabelColumn() {
        GridColumn column = new GridColumn( viewer.getGrid(), SWT.NONE);
        column.setWidth( 180 );
//        column.setImage( image );
//        column.setSort( SWT.DOWN );
//        column.setFooterImage( image );
        column.setAlignment( SWT.LEFT );
//        column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS", 16, SWT.NORMAL ) );
//        column.setFooterFont( new Font( column.getDisplay(), "Segoe Script", 16, SWT.NORMAL ) );
//        column.setSummary( false );
//        column.setMinimumWidth( 100 );
        column.setText("");	
	}

	/**
	 * 创建某一年的列，展开该年的月份
	 * 
	 * @param year
	 * @param month
	 */
	private void createGridColumn(int year, int month) {

		for (int i = 0; i < 12; i++) {
			createMonthGroup(year, i, month == i);
		}

	}

	private void createMonthGroup(int year, int month, boolean expand) {
		GridColumnGroup group = new GridColumnGroup(viewer.getGrid(),SWT.TOGGLE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.SHORT, RWT.getLocale());
		group.setText(monthName);

		int days = calendar.getActualMaximum(Calendar.DATE);
		for (int i = 0; i < days; i++) {
			createDayColumn(group,i);
		}
		createDetailColumn(group,year,month);

		group.setExpanded(!expand);
	}

	private void createDetailColumn(GridColumnGroup group, int year, int month) {
        GridColumn column = new GridColumn( group, SWT.NONE );
        column.setWidth( 80 );
//        column.setImage( image );
//        column.setSort( SWT.DOWN );
//        column.setFooterImage( image );
        column.setAlignment( SWT.CENTER );
//        column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS", 16, SWT.NORMAL ) );
//        column.setFooterFont( new Font( column.getDisplay(), "Segoe Script", 16, SWT.NORMAL ) );
//        column.setSummary( false );
//        column.setMinimumWidth( 100 );
        column.setDetail( true );
        column.setText("合计");		
	}

	private void createDayColumn(GridColumnGroup group, int i) {
        GridColumn column = new GridColumn( group, SWT.NONE );
        column.setWidth( 40 );
//        column.setImage( image );
//        column.setSort( SWT.DOWN );
//        column.setFooterImage( image );
        column.setAlignment( SWT.CENTER );
//        column.setHeaderFont( new Font( column.getDisplay(), "Comic Sans MS", 16, SWT.NORMAL ) );
//        column.setFooterFont( new Font( column.getDisplay(), "Segoe Script", 16, SWT.NORMAL ) );
//        column.setSummary( false );
//        column.setMinimumWidth( 100 );
        column.setDetail( false);
        column.setText(""+(i+1));
        
//        GridViewerColumn vColumn = new GridViewerColumn( viewer, column );
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
		// TODO Auto-generated method stub

	}

}
