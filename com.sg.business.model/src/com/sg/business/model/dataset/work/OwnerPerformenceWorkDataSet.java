package com.sg.business.model.dataset.work;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class OwnerPerformenceWorkDataSet extends
		SingleDBCollectionDataSetFactory {

	public OwnerPerformenceWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);

		BasicDBObject condition = new BasicDBObject();
		// 当前用户的
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();

		condition.put(Work.F_CHARGER, userId);
		// 只需要进行中的
		condition.put(Work.F_LIFECYCLE, Work.STATUS_WIP_VALUE);
		
		//2014.6.24  工时计量方式是 填报工时
		condition.put(Work.F_MEASUREMENT, Work.MEASUREMENT_TYPE_COMMIT_ID);

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
