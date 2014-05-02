package com.sg.business.performence.model;

import java.util.Calendar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class MonthInputDialog extends Dialog {

	private Spinner yearSpinner;
	private Combo monthCombo;
	private int year;
	private int month;

	public MonthInputDialog(Shell shell) {
		super(shell);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("请输入查询的年份和月份");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		yearSpinner = new Spinner(composite, SWT.BORDER);
		yearSpinner.setMinimum(2000);
		yearSpinner.setMaximum(2100);
		Calendar today=Calendar.getInstance();
		yearSpinner.setSelection(today.get(Calendar.YEAR));

		monthCombo = new Combo(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.widthHint = 80;
		String[] items = new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "10", "11", "12" };
		monthCombo.setItems(items);
		int index = today.get(Calendar.MONTH);
		monthCombo.select(index);
		return composite;
	}
	
	
	public int getYear(){
		return year;
	}
	
	public int getMonth(){
		return month;
	}
	
	@Override
	protected void okPressed() {
		year = yearSpinner.getSelection();
		month = monthCombo.getSelectionIndex();
		super.okPressed();
	}
}
