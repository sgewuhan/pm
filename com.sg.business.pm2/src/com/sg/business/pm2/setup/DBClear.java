package com.sg.business.pm2.setup;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.sg.business.model.CalendarSetting;
import com.sg.business.model.IModelConstants;

public class DBClear implements Runnable {
//	private static final ObjectId[] NOTDELETE = new ObjectId[] { null,
//			new ObjectId("5281cb76e0cc49249f3c6715"),
//			new ObjectId("5284340fe0cc7c1c547d44fa"),
//			new ObjectId("5281d483e0cc49249f3c68e7"),
//			new ObjectId("528476a0e0cc7c1c547d58d5"),
//			new ObjectId("52844a07e0cc7c1c547d4a82"),
//			new ObjectId("5282dac9e0ccf8afc27a1a3e") };

	@Override
	public void run() {
		 clearData();

	}


	// private void clearData() {
	// // ���������
	// DBCollection col = getCol(IModelConstants.C_BULLETINBOARD);
	// col.drop();
	//
	// // �����Ŀ������������
	// col = getCol(IModelConstants.C_CALENDAR_SETTING);
	// col.remove(new BasicDBObject().append(CalendarSetting.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // �����˾�����Ĺ������
	// col = getCol(IModelConstants.C_COMPANY_WORKORDER);
	// col.drop();
	//
	// // ���������
	// col = getCol(IModelConstants.C_DELIEVERABLE);
	// col.remove(new BasicDBObject().append(Deliverable.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // ����ĵ�
	// col = getCol(IModelConstants.C_DOCUMENT);
	// col.remove(new BasicDBObject().append(Document.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // ���Ŀ¼
	// col = getCol(IModelConstants.C_FOLDER);
	// col.remove(new BasicDBObject().append(Folder.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // // ��������ĵ�ģ������ı��
	// // // col = getCol(IModelConstants.C__IDS);
	// // // col.remove(new BasicDBObject().append("name",
	// // // new BasicDBObject().append("$ne", "documenttemplatenumber")));
	//
	// // ���log
	// col = getCol("log");
	// col.drop();
	//
	// // �����Ϣ
	// col = getCol(IModelConstants.C_MESSAGE);
	// col.drop();
	//
	// // �����Ŀ
	// col = getCol(IModelConstants.C_PROJECT);
	// col.remove(new BasicDBObject().append(Project.F__ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // �����ĿԤ��
	// col = getCol(IModelConstants.C_PROJECT_BUDGET);
	// col.remove(new BasicDBObject().append(ProjectBudget.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // �����Ŀ��ɫ
	// col = getCol(IModelConstants.C_PROJECT_ROLE);
	// col.remove(new BasicDBObject().append(ProjectRole.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // �����Ŀ��ɫָ��
	// col = getCol(IModelConstants.C_PROJECT_ROLE_ASSIGNMENT);
	// col.remove(new BasicDBObject().append(ProjectRoleAssignment.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // ����з��ɱ�
	// col = getCol(IModelConstants.C_RND_PEROIDCOST_COSTCENTER);
	// col.drop();
	//
	// // ����з��ɱ���̯
	// col = getCol(IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
	// col.drop();
	//
	// // ���work
	// col = getCol(IModelConstants.C_WORK);
	// col.remove(new BasicDBObject().append(Work.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // ���workǰ����
	// col = getCol(IModelConstants.C_WORK_CONNECTION);
	// col.remove(new BasicDBObject().append(WorkConnection.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // �����ʱ����
	// col = getCol(IModelConstants.C_WORKS_ALLOCATE);
	// col.remove(new BasicDBObject().append(WorksAllocate.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// // ���ʵ�ʹ�ʱ
	// col = getCol(IModelConstants.C_WORKS_PERFORMENCE);
	// col.remove(new BasicDBObject().append(WorksPerformence.F_PROJECT_ID,
	// new BasicDBObject().append("$nin", NOTDELETE)));
	//
	// col = getCol(IModelConstants.C_WORK);
	// DBCursor cursor = col.find();
	// List<ObjectId> workIds = new ArrayList<ObjectId>();
	// while(cursor.hasNext()){
	// DBObject object = cursor.next();
	// ObjectId _id = (ObjectId) object.get(Work.F__ID);
	// workIds.add(_id);
	// }
	//
	// // ����û�����
	// col = getCol(IModelConstants.C_USERTASK);
	// col.remove(new BasicDBObject().append(UserTask.F_WORK_ID,
	// new BasicDBObject().append("$nin", workIds)));
	//
	// // ����û��������
	// col = getCol("account");
	// col.update(
	// new BasicDBObject(),
	// new BasicDBObject().append("$unset",
	// new BasicDBObject().append("lastopened", "")), false,
	// true);
	// }

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
