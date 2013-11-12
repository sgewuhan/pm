package com.sg.business.model.dataset.work;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.part.CurrentAccountContext;

public class OwnerWorkDataSet extends SingleDBCollectionDataSetFactory {

	public OwnerWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);

		BasicDBObject condition = new BasicDBObject();
		// ��ǰ�û���
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();

		condition.put(Work.F_CHARGER, userId);
		// ֻ��Ҫ�����к�����ɵ�
		BasicDBObject wip = new BasicDBObject().append(Work.F_LIFECYCLE, Work.STATUS_WIP_VALUE);
		BasicDBObject finish = new BasicDBObject().append(Work.F_LIFECYCLE,Work.STATUS_FINIHED_VALUE);
		condition.put("$or", new Object[] { wip, finish });

		setQueryCondition(condition);
		setSort(new BasicDBObject().append(Work.F__ID, -1));
	}

}
