package com.sg.business.model.dataset.work;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;

public class OwnerPerformenceWorkDataSet extends
		SingleDBCollectionDataSetFactory {

	public OwnerPerformenceWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);

		BasicDBObject condition = new BasicDBObject();
		// 当前用户的
		String userId = getContext().getAccountInfo().getConsignerId();

		condition.put(Work.F_CHARGER, userId);
		// 只需要进行中的
		condition.put(Work.F_LIFECYCLE, Work.STATUS_WIP_VALUE);

		setQueryCondition(condition);
		setSort(new BasicDBObject().append(Work.F__ID, -1));
	}

	@Override
	protected boolean filter(PrimaryObject po) {
		if(po instanceof Work){
			return ((Work) po).isSummaryWork();
		}
		return super.filter(po);
	}
}
