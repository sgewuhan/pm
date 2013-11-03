package com.sg.business.model.dataset.usertask;

import org.jbpm.task.Status;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserTask;
import com.sg.widgets.part.CurrentAccountContext;

public class OwnerReservedTaskDataSet extends SingleDBCollectionDataSetFactory {

	public OwnerReservedTaskDataSet() {
		super(IModelConstants.DB, IModelConstants.C_USERTASK);
		
		String userId = new CurrentAccountContext().getConsignerId();
		
		DBObject query = new BasicDBObject();
		query.put(UserTask.F_USERID, userId);
		query.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		query.put("$or", new BasicDBObject[]{
				new BasicDBObject().append(UserTask.F_STATUS, Status.Reserved.name()),
				new BasicDBObject().append(UserTask.F_STATUS, Status.InProgress.name())
		});
		
		setQueryCondition(query);
		
		DBObject sort =  new BasicDBObject();
		sort.put(UserTask.F__ID, -1);
		setSort(sort );
	}

}
