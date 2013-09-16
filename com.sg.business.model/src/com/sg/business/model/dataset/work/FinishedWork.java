package com.sg.business.model.dataset.work;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Work;
import com.sg.widgets.MessageUtil;

public class FinishedWork extends SingleDBCollectionDataSetFactory{

	public FinishedWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
	}

	/**
	 * �������ݼ�
	 */
	@Override
	public DataSet getDataSet() {
		DBCollection col = getCollection();
		DBCursor cur = col.find(getQueryCondition(),
				new BasicDBObject().append(Work.F_ROOT_ID, 1));
		List<PrimaryObject> ret = new ArrayList<PrimaryObject>();
		cur.sort(getSort());
		while(cur.hasNext()){
			DBObject dbo = cur.next();
			ObjectId rootId = (ObjectId) dbo.get(Work.F_ROOT_ID);
			if(rootId==null){
				rootId = (ObjectId) dbo.get(Work.F__ID);
			}
			Work work = ModelService.createModelObject(Work.class, rootId);
			if(!ret.contains(work)){
				ret.add(work);
			}
		}
		
		return new DataSet(ret);
		
	}

	/**
	 * ��ȡ��ǰ�˺Ÿ���Ĺ����Ͳ���Ĺ���
	 * 
	 * @return ���ص�ǰ�˺Ÿ���Ĺ����Ͳ���Ĺ����� Ϊ{@link com.mongodb.DBObject}���͵�����
	 */
	@Override
	public DBObject getQueryCondition() {
		// ��õ�ǰ�ʺ�
		try {
			AccountInfo account = UserSessionContext.getAccountInfo();
			String userid = account.getConsignerId();
			// ��ѯ���˲���Ĺ���
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(Work.F_PARTICIPATE, userid);
			// ��������״̬Ϊ׼����������
			queryCondition
					.put(Work.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] {
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
