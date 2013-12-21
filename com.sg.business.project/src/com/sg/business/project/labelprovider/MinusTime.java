package com.sg.business.project.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.UserTask;
import com.sg.business.project.nls.Messages;

public class MinusTime extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof UserTask) {
			UserTask userTask = (UserTask) element;
			Object inProgress = userTask.getValue("InProgress"); //$NON-NLS-1$
			if (inProgress instanceof Date) {
				Date date = (Date) inProgress;
				long minus = date.getTime() - userTask.get_cdate().getTime();
				long hh = minus / (1000 * 60 * 60);
				long mm = (minus - hh * 1000 * 60 * 60) / (1000 * 60);
				long ss = (minus - hh * 1000 * 60 * 60 - mm * 1000 * 60) / 1000;
				return hh + Messages.get().MinusTime_1 + mm + Messages.get().MinusTime_2 + ss + Messages.get().MinusTime_3;

			} else {
				long minus = new Date().getTime()
						- userTask.get_cdate().getTime();
				long hh = minus / (1000 * 60 * 60);
				long mm = (minus - hh * 1000 * 60 * 60) / (1000 * 60);
				long ss = (minus - hh * 1000 * 60 * 60 - mm * 1000 * 60) / 1000;
				return hh + Messages.get().MinusTime_4 + mm + Messages.get().MinusTime_5 + ss + Messages.get().MinusTime_6;
			}

		}
		return ""; //$NON-NLS-1$
	}

}
