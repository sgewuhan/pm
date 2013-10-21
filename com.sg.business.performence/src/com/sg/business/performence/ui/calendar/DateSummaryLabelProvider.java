package com.sg.business.performence.ui.calendar;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IWorksSummary;
import com.sg.business.model.User;
import com.sg.widgets.Widgets;

public class DateSummaryLabelProvider extends ColumnLabelProvider {

	private int currentYear;
	private int month;
	private int dayOfMonth;
	private double workingHours;

	public DateSummaryLabelProvider(int currentYear, int month, int dayOfMonth) {
		this.currentYear = currentYear;
		this.month = month;

		this.dayOfMonth = dayOfMonth;
	}

	private double getSummary(Object element) {

		IWorksSummary ws = ((PrimaryObject) element)
				.getAdapter(IWorksSummary.class);
		if (ws == null) {
			return 0d;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, currentYear);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, dayOfMonth);
		Date date = cal.getTime();

		Calendar today = Calendar.getInstance();
		if (today.get(Calendar.YEAR) <= currentYear
				&& today.get(Calendar.MONTH) <= month
				&& today.get(Calendar.DATE) <= dayOfMonth) {
			return ws.getWorksAllocateSummaryOfDay(date);
		} else {
			return ws.getWorkPerformenceSummaryOfDay(date);
		}
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		// if (currentYear == 2013 && month == 9 && dayOfMonth == 22) {
		// System.out.println(element);
		// System.out.println();
		// }

		double summary = getSummary(element);
		if (summary < 1) {
			cell.setText("");
		} else {
			DecimalFormat df = new DecimalFormat("#########");
			cell.setText(df.format(summary));
		}

		if (element instanceof User) {
			if (summary > workingHours) {
				cell.setForeground(Widgets.getColor(cell.getControl().getDisplay(),
						255, 0, 0));
			}
		}
		
		if (workingHours == 0) {
			cell.setBackground(Widgets.getColor(cell.getControl()
					.getDisplay(), 225, 225, 225));
		} else if (isToday()) {
			cell.setBackground(Widgets.getColor(cell.getControl()
					.getDisplay(), 0xe2, 0xf0, 0xb6));
		}
	}

	public boolean isToday() {
		Calendar cal = Calendar.getInstance();
		return (cal.get(Calendar.YEAR) == currentYear)
				&& (cal.get(Calendar.MONTH) == month)
				&& (cal.get(Calendar.DATE) == dayOfMonth);
	}

	public void setWorkingHours(double hours) {
		workingHours = hours;
	}

}
