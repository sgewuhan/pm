package com.sg.business.model.bson;

import com.mobnut.db.model.IBSONProvider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;

/**
 * 公告板数据分拣机
 * @author gdiyang
 *
 */
public class BulletinBoardDateSorter implements IBSONProvider {

	/**
	 * 设置排序方式，根据发布日期进行排序
	 */
	@Override
	public DBObject getBSON() {
		return new BasicDBObject().append(BulletinBoard.F_PUBLISH_DATE, -1);
	}

}
