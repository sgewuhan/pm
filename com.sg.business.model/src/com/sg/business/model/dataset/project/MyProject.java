package com.sg.business.model.dataset.project;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

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
			String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
			// ��ѯ����Ϊ���˸������Ŀ�ͱ��˲������Ŀ
			DBObject queryCondition = new BasicDBObject();
			queryCondition.put(
					"$or",
					new BasicDBObject[] {
							new BasicDBObject().append(Project.F_CHARGER,
									userId),
							new BasicDBObject().append(Project.F_PARTICIPATE,
									userId) });
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
	}

}
