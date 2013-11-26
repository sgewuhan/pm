package com.sg.business.project.labelprovider;

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
			
			
			DBObject reserved = collection.findOne(new BasicDBObject().append(
					UserTask.F_WORKITEMID, value).append(UserTask.F_STATUS,
					"Reserved"));
			
			DBObject inProgress = collection.findOne(new BasicDBObject()
					.append(UserTask.F_WORKITEMID, value).append(
							UserTask.F_STATUS, "InProgress"));
			
			if(reserved!=null&&inProgress!=null){
				UserTask taskReserved = ModelService.createModelObject(reserved,
						UserTask.class);
				
				UserTask taskInProgress = ModelService.createModelObject(inProgress,
						UserTask.class);
				
			    	long intime = taskInProgress.get_cdate().getTime();
			    	long retime =taskReserved.get_cdate().getTime();
			    	long minus=intime-retime;
			    	long hh =minus/(1000*60*60);
			    	long mm=(minus-hh*1000*60*60)/(1000*60);
			    	long ss=(minus-hh*1000*60*60-mm*1000*60)/1000;
			    	return hh+"–° ±"+mm+"∑÷÷”"+ ss+"√Î";
			}

		}
		return "";
	}

}
