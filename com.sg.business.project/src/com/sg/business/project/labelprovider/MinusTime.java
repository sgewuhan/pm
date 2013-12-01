package com.sg.business.project.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserTask;

public class MinusTime extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof UserTask) {
			UserTask userTask = (UserTask) element;
			Object value = userTask.getValue(UserTask.F_WORKITEMID);
			DBCollection collection = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_USERTASK);

			DBObject inProgress = collection.findOne(new BasicDBObject()
					.append(UserTask.F_WORKITEMID, value).append(
							UserTask.F_STATUS, "InProgress"));

			if (inProgress != null) {
				UserTask taskInProgress = ModelService.createModelObject(
						inProgress, UserTask.class);
				long minus = taskInProgress.get_cdate().getTime()
						- userTask.get_cdate().getTime();
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
