package com.sg.business.work.view;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.sg.widgets.part.IRefreshablePart;

public class DeptWork extends ViewPart implements IRefreshablePart{

	private GridTreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
	    viewer = new GridTreeViewer( parent, SWT.FULL_SELECTION );
	    viewer.getGrid().setHeaderVisible( true );
	    int year = Calendar.getInstance().get(Calendar.YEAR);
	    int month = Calendar.getInstance().get(Calendar.MONTH);
	    createYearGroup(year,month);
	}

	/**
	 * 创建某一年的列，展开该年的月份
	 * @param year
	 * @param month
	 */
	private void createYearGroup(int year, int month) {
		// TODO Auto-generated method stub
		
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
