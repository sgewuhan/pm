package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;

/**
 * ��������ݷּ��
 * @author gdiyang
 *
 */
public class BulletinBoardDateSorter implements IBSONProvider {

	/**
	 * ��������ʽ�����ݷ������ڽ�������
	 */
	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append(BulletinBoard.F_PUBLISH_DATE, -1);
	}

}
