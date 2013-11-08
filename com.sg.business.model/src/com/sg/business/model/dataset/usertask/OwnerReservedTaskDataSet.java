package com.sg.business.model.dataset.usertask;

import org.jbpm.task.Status;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserTask;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class OwnerReservedTaskDataSet extends SingleDBCollectionDataSetFactory {

	private DBCollection workCol;

	public OwnerReservedTaskDataSet() {
		super(IModelConstants.DB, IModelConstants.C_USERTASK);
		workCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK);

		String userId = new CurrentAccountContext().getConsignerId();

		DBObject query = new BasicDBObject();
		query.put(UserTask.F_USERID, userId);
		query.put(UserTask.F_LIFECYCLE_CHANGE_FLAG, Boolean.FALSE);
		query.put(
				"$or",
				new BasicDBObject[] {
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.Reserved.name()),
						new BasicDBObject().append(UserTask.F_STATUS,
								Status.InProgress.name()) });

		setQueryCondition(query);

		DBObject sort = new BasicDBObject();
		sort.put(UserTask.F__ID, -1);
		setSort(sort);
	}

	@Override
	protected PrimaryObject getDataItem(DBObject dbo) {
		Object work_id = dbo.get(UserTask.F_WORK_ID);
		long cnt = workCol.count(new BasicDBObject().append(Work.F__ID, work_id)
				.append(Work.F_LIFECYCLE, Work.STATUS_WIP_VALUE));
		if(cnt>0){
			return ModelService.createModelObject(dbo, UserTask.class);
		}else{
			return null;
		}
	}

}
