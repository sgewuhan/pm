package com.sg.business.project.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.UserTask;

public class MinusTime extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof UserTask) {
			UserTask userTask = (UserTask) element;
			Object inProgress = userTask.getValue("InProgress");
			if (inProgress instanceof Date) {
				Date date = (Date) inProgress;
				long minus = date.getTime() - userTask.get_cdate().getTime();
				long hh = minus / (1000 * 60 * 60);
				long mm = (minus - hh * 1000 * 60 * 60) / (1000 * 60);
				long ss = (minus - hh * 1000 * 60 * 60 - mm * 1000 * 60) / 1000;
				return hh + "小时" + mm + "分钟" + ss + "秒";

			} else {
				long minus = new Date().getTime()
						- userTask.get_cdate().getTime();
				long hh = minus / (1000 * 60 * 60);
				long mm = (minus - hh * 1000 * 60 * 60) / (1000 * 60);
				long ss = (minus - hh * 1000 * 60 * 60 - mm * 1000 * 60) / 1000;
				return hh + "小时" + mm + "分钟" + ss + "秒";
			}

		}
		return "";
	}

}
