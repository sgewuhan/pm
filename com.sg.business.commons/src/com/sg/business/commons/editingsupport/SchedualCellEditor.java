package com.sg.business.commons.editingsupport;

import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class SchedualCellEditor extends CellEditor {

	private Button button;
	private Object value;

	public SchedualCellEditor(Composite control) {
		super(control);
	}

	@Override
	protected Control createControl(Composite parent) {
		button = new Button(parent, SWT.PUSH);
		button.setBackground(new Color(null,255,255,255));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("ok");
			}
		});
		return button;
	}

	@Override
	protected Object doGetValue() {
		return new Date();
	}

	@Override
	protected void doSetFocus() {
		button.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		this.value = value;
	}

	@Override
	protected int getDoubleClickTimeout() {
		return 0;
	}
}
