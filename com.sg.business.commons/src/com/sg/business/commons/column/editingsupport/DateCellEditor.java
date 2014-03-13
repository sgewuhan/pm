package com.sg.business.commons.column.editingsupport;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import com.sg.widgets.part.DateTime2;

public class DateCellEditor extends CellEditor {

	private DateTime dateTime;

	public DateCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Control createControl(Composite parent) {
		dateTime = new DateTime2(parent, getStyle());
		return dateTime;
	}

	@Override
	protected Object doGetValue() {
		Calendar c = Calendar.getInstance();
		c.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(),
				dateTime.getHours(), dateTime.getMinutes(),
				dateTime.getSeconds());
		return c.getTime();
	}

	@Override
	protected void doSetFocus() {
		if (dateTime != null) {
			dateTime.setFocus();
		}
	}

	@Override
	protected void doSetValue(Object value) {
		Date date = (Date) value;
		if (date != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			dateTime.setYear(c.get(Calendar.YEAR));
			dateTime.setMonth(c.get(Calendar.MONTH));
			dateTime.setDay(c.get(Calendar.DATE));
			dateTime.setHours(c.get(Calendar.HOUR_OF_DAY));
			dateTime.setMinutes(c.get(Calendar.MINUTE));
			dateTime.setSeconds(c.get(Calendar.SECOND));
		}
	}

}
