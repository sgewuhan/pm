package com.sg.business.work.filter;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.sg.widgets.part.DateTime2;

public class DateFromToSelector extends Dialog {

	private Date[] fromTo ;

	protected DateFromToSelector(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(4, false));

		Label label = new Label(composite, SWT.NONE);
		label.setText("ÆðÊ¼:");

		final DateTime2 dt = new DateTime2(parent, SWT.BORDER | SWT.MEDIUM);
		dt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Calendar c = Calendar.getInstance();
				c.set(dt.getYear(), dt.getMonth(),
						dt.getDay(), dt.getHours(),
						dt.getMinutes(), dt.getSeconds());
				if(fromTo == null){
					fromTo = new Date[2];
				}
				fromTo[0] = c.getTime();
				super.widgetSelected(e);
			}
		});

		return composite;
	}

	public Date[] getDate() {
		return fromTo;
	}

}
