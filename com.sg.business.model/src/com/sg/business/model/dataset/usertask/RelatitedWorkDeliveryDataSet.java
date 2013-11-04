package com.sg.business.model.dataset.usertask;

import org.bson.types.ObjectId;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.UserTask;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class RelatitedWorkDeliveryDataSet extends MasterDetailDataSetFactory {

	public RelatitedWorkDeliveryDataSet() {
		super(IModelConstants.DB, IModelConstants.C_DELIEVERABLE);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Deliverable.F_WORK_ID;
	}

	@Override
	protected Object getMasterValue() {
		ObjectId workId = ((UserTask) master).getWorkId();
		return workId;
	}

	@Override
	protected PrimaryObject getDataItem(DBObject dbo) {
		ObjectId docId = (ObjectId) dbo.get(Deliverable.F_DOCUMENT_ID);
		return ModelService.createModelObject(Document.class, docId);

	}
}
