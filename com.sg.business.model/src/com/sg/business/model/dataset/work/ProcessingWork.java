package com.sg.business.model.dataset.work;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.Assert;

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

public class ProcessingWork extends SingleDBCollectionDataSetFactory {

	public ProcessingWork() {
		super(IModelConstants.DB, IModelConstants.C_WORK);
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
			String userid = account.getconsignerId();
			// ��ѯ���˲���Ĺ���
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(Work.F_PARTICIPATE, userid);
			// ��������״̬Ϊ׼����������
			queryCondition
					.put(Work.F_LIFECYCLE,
							new BasicDBObject().append("$in", new String[] {
									Work.STATUS_ONREADY_VALUE,
									Work.STATUS_WIP_VALUE }));
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append(Work.F__ID, null);
		}
	}

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		DBCollection c = getCollection();
		Assert.isNotNull(c, "�޷���ȡ����");

		DBObject query = getQueryCondition();
		DBObject projection = getProjection();
		DBCursor cursor = c.find(query, projection);


		List<PrimaryObject> dataItems = new ArrayList<PrimaryObject>();
		Class<? extends PrimaryObject> clas;

		Iterator<DBObject> iter = cursor.iterator();

		while (iter.hasNext()) {
			DBObject dbo = iter.next();
			Object obj = dbo.get(Work.F_ROOT_ID);
			
			
			
			clas = getModelClass(dbo);
			Assert.isNotNull(clas, "���������Ϊ��");
			PrimaryObject po = ModelService.createModelObject(dbo, clas);
			/*while(po.getParentPrimaryObject()!=null){
				po=po.getParentPrimaryObject();
			}*/
			po.setDataSet(ds);
			Assert.isNotNull(po, "�޷�����ORMӳ��Ķ���:" + dbo.toString());
			dataItems.add(po);
		}

		return dataItems;
	}

	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Work.F__ID, -1);
	}
}
