package com.sg.business.model.dataset.project;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectBulletinBoardDataSet extends MasterDetailDataSetFactory {

	public ProjectBulletinBoardDataSet() {
		//���ù�������ݺϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_BULLETINBOARD);
		BasicDBObject condition = new BasicDBObject();
		condition.put(BulletinBoard.F_PARENT_BULLETIN,null);
		setQueryCondition(condition);
	}

	/**
	 * ��������
	 */
	@Override
	public DBObject getSort() {
		//���ݷ������ڽ�������
		return new BasicDBObject().append(BulletinBoard.F_PUBLISH_DATE, -1);
	}
	
	@Override
	protected String getDetailCollectionKey() {
		return BulletinBoard.F_PROJECT_ID;
	}

}
