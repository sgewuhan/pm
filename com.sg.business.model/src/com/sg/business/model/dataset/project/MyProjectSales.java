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
 * ���ڹ���ǰ�û������������Ŀ
 * 
 * @author yangjun
 * 
 */
public class MyProjectSales extends SingleDBCollectionDataSetFactory {

	private String userId;

	/**
	 * ��Ŀ�������캯��������������Ŀ�����Ĵ�����ݿ⼰���ݴ洢��
	 */
	public MyProjectSales() {
		// ������Ŀ�����Ĵ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
	}

	@Override
	public DBObject getQueryCondition() {
		// ��õ�ǰ�ʺ�
		try {
			// ��ѯ����Ϊ���˸������Ŀ�ͱ��˲������Ŀ
			DBObject queryCondition = createQueryCondition();
			queryCondition.put(Project.F_BUSINESS_CHARGER, userId);
			return queryCondition;

		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null); //$NON-NLS-1$
		}
	}

	@Override
	public DBObject getSort() {
		return new BasicDBObject().append(Project.F_ACTUAL_START, -1).append(Project.F_PLAN_START, -1); //$NON-NLS-1$
	}
}
