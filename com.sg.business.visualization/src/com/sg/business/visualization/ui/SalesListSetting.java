package com.sg.business.visualization.ui;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;

import com.sg.business.resource.nls.Messages;
import com.sg.business.visualization.view.AbstractSalesListView;

public class SalesListSetting extends Shell {

	private AbstractSalesListView profitListView;
	private int limitNumber;
	private int month;
	private int year;

	public SalesListSetting(Shell parent, AbstractSalesListView profitListView) {
		super(parent, SWT.BORDER);
		this.profitListView = profitListView;
		limitNumber = profitListView.getLimitNumber();
		year = profitListView.getYear();
		month = profitListView.getMonth();
		createContent();
	}

	private void createContent() {
		setLayout(new FormLayout());
		final Combo yearCombo = new Combo(this, SWT.READ_ONLY);
		FormData fd = new FormData();
		yearCombo.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(0, 4);
		fd.width = 100;

		final int startYear = Calendar.getInstance().get(Calendar.YEAR) - 5;
		for (int i = startYear; i < (startYear + 6); i++) {
			yearCombo.add("" + i); //$NON-NLS-1$
		}
		yearCombo.select(year - startYear);

		final Combo monthCombo = new Combo(this, SWT.READ_ONLY);
		fd = new FormData();
		monthCombo.setLayoutData(fd);
		fd.left = new FormAttachment(yearCombo, 4);
		fd.top = new FormAttachment(0, 4);
		fd.width = 80;
		for (int i = 1; i < 13; i++) {
			monthCombo.add("" + i + Messages.get().SalesListSetting_A_1); //$NON-NLS-1$
		}
		monthCombo.select(month - 1);

		final Combo limitNumberCombo = new Combo(this, SWT.READ_ONLY);
		fd = new FormData();
		limitNumberCombo.setLayoutData(fd);
		fd.left = new FormAttachment(monthCombo, 4);
		fd.top = new FormAttachment(0, 4);
		fd.right = new FormAttachment(100, -4);

		fd.width = 80;
		for (int i = 1; i < 5; i++) {
			limitNumberCombo.add("" + i * 5 + Messages.get().SalesListSetting_A_3); //$NON-NLS-1$
		}
		limitNumberCombo.select(limitNumber / 5 - 1);

		Button ok = new Button(this, SWT.PUSH);
		ok.setText(Messages.get().SalesListSetting_A_4);
		fd = new FormData();
		ok.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(limitNumberCombo, 16);
		fd.bottom = new FormAttachment(100, -4);
		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int yearIndex = yearCombo.getSelectionIndex();
				int monthIndex = monthCombo.getSelectionIndex();
				int limitNumberIndex = limitNumberCombo.getSelectionIndex();
				setFilter(startYear + yearIndex, monthIndex + 1,
						limitNumberIndex);
				dispose();
			}
		});

		Button cancel = new Button(this, SWT.PUSH);
		cancel.setText(Messages.get().SalesListSetting_A_5);
		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dispose();
			}
		});

		fd = new FormData();
		cancel.setLayoutData(fd);
		fd.left = new FormAttachment(ok, 4);
		fd.top = new FormAttachment(limitNumberCombo, 16);
		fd.bottom = new FormAttachment(100, -4);

		addShellListener(new ShellListener() {

			@Override
			public void shellDeactivated(ShellEvent e) {
				close();
			}

			@Override
			public void shellClosed(ShellEvent e) {
			}

			@Override
			public void shellActivated(ShellEvent e) {
			}
		});
	}

	public void open(Point location) {
		pack();
		setLocation(location);
		super.open();
	}

	protected void setFilter(int yearIndex, int monthIndex, int limitNumberIndex) {
		Calendar cal = Calendar.getInstance();
		if (yearIndex < 0) {
			return;
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		if (year == yearIndex && monthIndex > month) {
			return;
		}
		profitListView.setYear(yearIndex);
		profitListView.setMonth(monthIndex);
		profitListView.setLimitNumber((limitNumberIndex + 1) * 5);
	}
}
