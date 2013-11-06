package com.sg.business.pm2.setup;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;

public class DBClear implements Runnable {

	@Override
	public void run() {

		clearData();
		

	}

	private void clearData() {
		// ���������
		DBCollection col = getCol(IModelConstants.C_BULLETINBOARD);
		col.drop();

		// �����Ŀ������������
		col = getCol(IModelConstants.C_CALENDAR_SETTING);
		col.remove(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID,
				new BasicDBObject().append("$ne", null)));

		// �����˾�����Ĺ������
		col = getCol(IModelConstants.C_COMPANY_WORKORDER);
		col.drop();

		// ���������
		col = getCol(IModelConstants.C_DELIEVERABLE);
		col.drop();

		// ����ĵ�
		col = getCol(IModelConstants.C_DOCUMENT);
		col.drop();

		// ���Ŀ¼
		col = getCol(IModelConstants.C_FOLDER);
		col.remove(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID,
				new BasicDBObject().append("$ne", null)));

		// ��������ĵ�ģ������ı��
		col = getCol(IModelConstants.C__IDS);
		col.remove(new BasicDBObject().append("name",
				new BasicDBObject().append("$ne", "documenttemplatenumber")));

		// ���log
		col = getCol("log");
		col.drop();

		// �����Ϣ
		col = getCol(IModelConstants.C_MESSAGE);
		col.drop();

		// �����Ŀ
		col = getCol(IModelConstants.C_PROJECT);
		col.drop();

		// �����ĿԤ��
		col = getCol(IModelConstants.C_PROJECT_BUDGET);
		col.drop();

		// �����Ŀ��ɫ
		col = getCol(IModelConstants.C_PROJECT_ROLE);
		col.drop();

		// �����Ŀ��ɫָ��
		col = getCol(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
		col.drop();

		// ����з��ɱ�
		col = getCol(IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
		col.drop();

		// ����з��ɱ���̯
		col = getCol(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		col.drop();

		// ���work
		col = getCol(IModelConstants.C_WORK);
		col.drop();

		// ���workǰ����
		col = getCol(IModelConstants.C_WORK_CONNECTION);
		col.drop();

		// �����ʱ����
		col = getCol(IModelConstants.C_WORKS_ALLOCATE);
		col.drop();

		// ���ʵ�ʹ�ʱ
		col = getCol(IModelConstants.C_WORKS_PERFORMENCE);
		col.drop();
		
		// ����û�����
		col = getCol(IModelConstants.C_USERTASK);
		col.drop();

		// ����û��������
		col = getCol("account");
		col.update(
				new BasicDBObject(),
				new BasicDBObject().append("$unset",
						new BasicDBObject().append("lastopened", "")), false,
				true);
	}

	private DBCollection getCol(String collectionName) {
		return DBActivator.getCollection(IModelConstants.DB, collectionName);
	}

}
