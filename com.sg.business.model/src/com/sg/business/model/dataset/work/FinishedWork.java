package com.sg.business.model.dataset.work;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

public class FinishedWork extends SingleDBCollectionDataSetFactory{

	private String userId;


	public FinishedWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
	}

//	/**
//	 * �������ݼ�
//	 */
//	@Override
//	public DataSet getDataSet() {
//		DBCollection col = getCollection();
//		DBCursor cur = col.find(getQueryCondition(),
//				new BasicDBObject().append(Work.F_ROOT_ID, 1));
//		List<PrimaryObject> ret = new ArrayList<PrimaryObject>();
//		cur.sort(getSort());
//		while(cur.hasNext()){
//			DBObject dbo = cur.next();
//			ObjectId rootId = (ObjectId) dbo.get(Work.F_ROOT_ID);
//			if(rootId==null){
//				rootId = (ObjectId) dbo.get(Work.F__ID);
//			}
//			Work work = ModelService.createModelObject(Work.class, rootId);
//			if(!ret.contains(work)){
//				ret.add(work);
//			}
//		}
//		
//		return new DataSet(ret);
//		
//	}

	/**
	 * ��ȡ��ǰ�˺Ÿ���Ĺ����Ͳ���Ĺ���
	 * 
	 * @return ���ص�ǰ�˺Ÿ���Ĺ����Ͳ���Ĺ����� Ϊ{@link com.mongodb.DBObject}���͵�����
	 */
	@Override
	public DBObject getQueryCondition() {
		// ��õ�ǰ�ʺ�
		try {
			

			// ��ѯ���˲���Ĺ���
			DBObject queryCondition = createQueryCondition();
			queryCondition.put(Work.F_PARTICIPATE, userId);
			// ��������״̬Ϊ׼����������
			queryCondition
					.put(Work.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] { //$NON-NLS-1$
									Work.STATUS_FINIHED_VALUE,
									Work.STATUS_CANCELED_VALUE}));
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append(Work.F__ID, null);
		}
	}


	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Work.F__ID, -1);
	}

}
