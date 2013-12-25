package com.sg.business.project.labelprovider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.UserTask;

public class CompleteDate extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		DateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss"); //$NON-NLS-1$
		if (element instanceof UserTask) {
			UserTask userTask = (UserTask) element;
			Object value = userTask.getValue("Completed"); //$NON-NLS-1$
			if (value instanceof Date) {
				return df.format((Date) value);
			}
		}
		return ""; //$NON-NLS-1$
	}

}
