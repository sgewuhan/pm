package com.sg.business.model.dataset.project;

import com.mobnut.db.model.AccountInfo;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;

/**
 * <p>
 * ��Ŀ����
 * </p>
 * �̳���{@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}��
 * ���ڹ���ǰ�û����������Ŀ�Ͳ������Ŀ
 * 
 * @author yangjun
 *
 */
public class MyProject extends SingleDBCollectionDataSetFactory {

	/**
	 * ��Ŀ�������캯��������������Ŀ�����Ĵ�����ݿ⼰���ݴ洢��
	 */
	public MyProject() {
		//������Ŀ�����Ĵ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
	}

	/**
	 * ��ȡ��ǰ�˺Ÿ������Ŀ�Ͳ������Ŀ
	 * 
	 * @return ���ص�ǰ�˺Ÿ������Ŀ�Ͳ������Ŀ��
	 * Ϊ{@link com.mongodb.DBObject}���͵�����
	 */
	@Override
	public DBObject getQueryCondition() {
		// ��õ�ǰ�ʺ�
		try {
			AccountInfo account = UserSessionContext.getAccountInfo();
			String userid = account.getConsignerId();
			// ��ѯ����Ϊ���˸������Ŀ�ͱ��˲������Ŀ
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(
					"$or",
					new BasicDBObject[] {
							new BasicDBObject().append(Project.F_CHARGER,
									userid),
							new BasicDBObject().append(Project.F_PARTICIPATE,
									userid) });
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
	}

}
