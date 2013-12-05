package com.sg.business.project.labelprovider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.UserTask;

public class InProgressDate extends ColumnLabelProvider {


	@Override
	public String getText(Object element) {
		DateFormat df = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		if (element instanceof UserTask) {
			UserTask userTask = (UserTask) element;
//			Object value = userTask.getValue(UserTask.F_WORKITEMID);
//			DBCollection collection = DBActivator.getCollection(
//					IModelConstants.DB, IModelConstants.C_USERTASK);
//			DBCursor cur = collection.find(new BasicDBObject().append(
//					UserTask.F_WORKITEMID, value).append(UserTask.F_STATUS,
//					"InProgress"));
//			while (cur.hasNext()) {
//				DBObject next = cur.next();
//				UserTask task = ModelService.createModelObject(next,
//						UserTask.class);
//				return df.format(task.get_cdate());
//			}
			
			return df.format(userTask.getValue("InProgress"));
		}
		return "";
	}

}
