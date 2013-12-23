package com.sg.business.model.dataset.project;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * ��Ŀ������
 * </p>
 * �̳���{@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory},
 * ������ʾ��ǰ�û����Խ��й������Ŀ<br/>
 * �������¹��ܣ� <li> <li> <li>
 * 
 * @author yangjun
 * 
 */
public class ManagementProjectRootWork extends SingleDBCollectionDataSetFactory {

	private User user;

	/**
	 * ��Ŀ�����Ϲ��캯��������������Ŀ�����ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public ManagementProjectRootWork() {
		// ������Ŀ�����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_WORK);
		// ��ȡ��ǰ�û���Ϣ
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		user = UserToolkit.getUserById(userId);
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> result = new ArrayList<PrimaryObject>();
		try {
			// ��õ�ǰ�ʺſɹ������Ŀְ����֯

			// ��ȡ��ǰ�û�����ҵ�����Ա��ɫ����֯
			List<PrimaryObject> orglist = user
					.getRoleGrantedInFunctionDepartmentOrganization(Role.ROLE_PROJECT_ADMIN_ID);
			ObjectId[] ids = new ObjectId[orglist.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = orglist.get(i).get_id();
			}
			// ��ȡ��ǰ�û�����ҵ�����Ա��ɫ����֯���µ���Ŀ
			BasicDBObject condition = new BasicDBObject();
			condition.put(Project.F_FUNCTION_ORGANIZATION,
					new BasicDBObject().append("$in", ids)); //$NON-NLS-1$

			// ����Ŀ��״̬�����ǽ�����
			condition.put(Project.F_LIFECYCLE, Project.STATUS_WIP_VALUE);

			DBCollection projectCol = DBActivator.getCollection(
					IModelConstants.DB, IModelConstants.C_PROJECT);
			DBCursor cur = projectCol.find(condition, new BasicDBObject()
					.append(Project.F_WORK_ID, 1).append(Project.F_DESC, 1));
			while (cur.hasNext()) {
				DBObject dbo = cur.next();
				BasicDBObject data = new BasicDBObject().append(Work.F__ID,
						dbo.get(Project.F_WORK_ID)).append(Work.F_DESC,
						dbo.get(Project.F_DESC));
				Work work = ModelService.createModelObject(data, Work.class);
				result.add(work);
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}

		return new DataSet(result);
	}

}
