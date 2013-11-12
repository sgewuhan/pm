package com.sg.business.model.dataset.bulletinboard;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.dataset.ContextSingleDataSetFactory;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;

/**
 * <p>
 * �����
 * </p>
 * �̳��� {@link SingleDBCollectionDataSetFactory} ���ڻ�ȡ�������Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ� <li>��ȡ�����������Ϣ <li>���ò�ѯ���� <li>��������
 * 
 * @author gdiyang
 * 
 */
public class BulletinBoardDataSet extends ContextSingleDataSetFactory {

	/**
	 * ����幹�캯��
	 */
	public BulletinBoardDataSet() {
		// ���ù�������ݺϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_BULLETINBOARD);
	}

	/**
	 * ���ò�ѯ����
	 */
	@Override
	public DBObject getQueryCondition() {
		try {
			// ��ȡ��ǰ�û����ڵ���֯
			List<ObjectId> orgIds = new ArrayList<ObjectId>();
			String userid = getContext().getAccountInfo().getConsignerId();
			User user = UserToolkit.getUserById(userid);
			Organization org = user.getOrganization();
			/**
			 * zhonghua �û�����֯����Ϊ��
			 */
			if (org != null) {
				// ��ȡ��ǰ�û�������֯���¼���֯
				searchDown(org, orgIds);
				// ��ȡ��ǰ�û�������֯���ϼ���֯
				searchUp(org, orgIds);
			}

			// ���ò�ѯ����
			BasicDBObject condition = new BasicDBObject();
			condition.put(BulletinBoard.F_ORGANIZATION_ID,
					new BasicDBObject().append("$in", orgIds));
			condition.put(BulletinBoard.F_PARENT_BULLETIN, null);
			condition.put(BulletinBoard.F_PROJECT_ID, null);
			return condition;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null);
		}
	}

	/**
	 * ��ȡ��֯���¼���֯
	 * 
	 * @param org
	 *            : ��ǰ��֯
	 * @param list
	 *            : ���ѯ����֯�б�
	 */
	private void searchDown(Organization org, List<ObjectId> list) {
		// ��ȡ��ǰ��֯���¼���֯
		List<PrimaryObject> children = org.getChildrenOrganization();
		// ѭ����������¼���֯�����ѯ����֯�б���
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			if (child.isFunctionDepartment()) {
				list.add(child.get_id());
			}
			searchDown(child, list);
		}
	}

	/**
	 * ��ȡ��֯���ϼ���֯
	 * 
	 * @param org
	 *            : ��ǰ��֯
	 * @param list
	 *            : ���ѯ����֯�б�
	 */
	private void searchUp(Organization org, List<ObjectId> list) {
		// ��ӵ�ǰ��֯����֯�б���
		list.add(0, org.get_id());
		// ��������ϼ���֯����֯�б���
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	/**
	 * ��������
	 */
	@Override
	public DBObject getSort() {
		// ���ݷ������ڽ�������
		return new BasicDBObject().append(BulletinBoard.F__ID, -1);
	}
}
