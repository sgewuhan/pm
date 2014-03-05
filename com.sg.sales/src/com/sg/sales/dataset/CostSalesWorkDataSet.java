package com.sg.sales.dataset;

import java.util.Date;

import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.sales.model.WorkCost;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * ��÷��÷����˵Ĺ���
 * 
 * @author Administrator
 * 
 */
public class CostSalesWorkDataSet extends MasterDetailDataSetFactory {

	public CostSalesWorkDataSet() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	@Override
	public void masterChanged(PrimaryObject master, PrimaryObject oldMaster,
			IWorkbenchPart part) {
		this.master = master;
		WorkCost workCost = (WorkCost) master;
		String ownerId = workCost.getCostOwnerId();
		Date startDate = workCost.getStartDate();
		Date finishDate = workCost.getFinishDate();
		BasicDBObject condition = new BasicDBObject();
		if (Utils.isNullOrEmpty(ownerId)) {
			condition.put(Work.F__ID, null);
		} else {
			// �����ĸ����ˣ�������
			BasicDBObject isOwner = new BasicDBObject()
					.append("$or",
							new BasicDBObject[] {
									new BasicDBObject().append(Work.F_CHARGER,
											ownerId),
									new BasicDBObject().append(
											Work.F_PARTICIPATE, ownerId) });
			// ��������״̬�ǽ����еĻ�����ͣ��,��ʼʱ��Ҫ���ڷ��÷����Ŀ�ʼʱ��
			BasicDBObject wip = new BasicDBObject().append(
					Work.F_LIFECYCLE,
					new BasicDBObject().append("$in", new String[] {
							Work.STATUS_WIP_VALUE, Work.STATUS_PAUSED_VALUE }))
					.append(Work.F_ACTUAL_START,
							new BasicDBObject().append("$lte", startDate));
			// ��������״̬ʱ��ɻ�ȡ���ģ����ʱ��Ҫ���ڷ��÷��������ʱ��
			BasicDBObject close = new BasicDBObject().append(
					Work.F_LIFECYCLE,
					new BasicDBObject().append("$in", new String[] {
							Work.STATUS_CANCELED_VALUE,
							Work.STATUS_FINIHED_VALUE })).append(
					Work.F_ACTUAL_FINISH,
					new BasicDBObject().append("$gte", finishDate));
			// ��ֹ���õĹ���
			BasicDBObject exception = new BasicDBObject().append(
					Work.F_EXPENSE_FORBIDDEN,
					new BasicDBObject().append("$ne", Boolean.TRUE));
			BasicDBObject status = new BasicDBObject().append("$or",
					new BasicDBObject[] { wip, close });
			condition.append("$and", new BasicDBObject[] { isOwner, status,
					exception });
		}
		setQueryCondition(condition);
	}

	@Override
	protected String getDetailCollectionKey() {
		return null;
	}

}
