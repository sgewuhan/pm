package com.sg.business.model.dataset.project;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * ��Ŀ������
 * </p>
 * �̳���{@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory},
 * ������ʾ��ǰ�û����Խ��й������Ŀ<br/>
 * �������¹��ܣ�
 * <li>
 * <li>
 * <li>
 * 
 * @author yangjun
 *
 */
public class ManagementProject extends SingleDBCollectionDataSetFactory {

	private User user;

	/**
	 * ��Ŀ�����Ϲ��캯��������������Ŀ�����ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public ManagementProject() {
		//������Ŀ�����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_PROJECT);
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	/**
	 * ��ȡ��ǰ�˺ſɹ���ľ�����Ŀ����ְ�ܵ���֯
	 * 
	 * @return ���ص�ǰ�û��ɹ���ľ�����Ŀ����ְ�ܵ���֯��
	 * Ϊ{@link com.mongodb.DBObject}���͵�����
	 */
	@Override
	public DBObject getQueryCondition() {
		// ��õ�ǰ�ʺſɹ������Ŀְ����֯
		try {
			//��ȡ��ǰ�û�������Ŀ����Ա��ɫ����֯
			List<PrimaryObject> orglist = user
					.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_PROJECT_ADMIN_ID);
			ObjectId[] ids = new ObjectId[orglist.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = orglist.get(i).get_id();
			}
			//��ȡ��ǰ�û�����ҵ�����Ա��ɫ����֯���µ���Ŀ
			BasicDBObject condition = new BasicDBObject();
			condition.put(Project.F_FUNCTION_ORGANIZATION,
					new BasicDBObject().append("$in", ids)); //$NON-NLS-1$
			return condition;
		} catch (Exception e) {
			MessageUtil.showToast(e);
			return new BasicDBObject().append("_id", null); //$NON-NLS-1$
		}
	}

}
