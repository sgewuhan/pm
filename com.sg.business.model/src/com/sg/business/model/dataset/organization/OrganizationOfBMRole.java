package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;

/**
 * ��õ�ǰ�û��Ŀ��Թ������֯
 * 
 * @author Administrator
 * 
 */
public class OrganizationOfBMRole extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		List<PrimaryObject> orgs = new ArrayList<PrimaryObject>();
		
		// ����¼��Ŀɹ������֯
		addRoleGrantedOrganization(currentUser,
				Role.ROLE_BUSINESS_ADMIN_ID, orgs);
		
		count = orgs.size();
		return orgs;
	}

	private void addRoleGrantedOrganization(User user,
			String roleNumber,  List<PrimaryObject> orgs) {
		// ����û��������roleNumber��Ӧ�Ľ�ɫ
		List<PrimaryObject> roles = user.getRoles(roleNumber);

		// ȡ����Щ��ɫ��������֯��id
		ObjectId[] orgIds = new ObjectId[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			orgIds[i] = ((Role) roles.get(i)).getOrganization_id();
		}

		// ��ѯ���ڿɹ������֯
		DBCollection orgCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject condition = new BasicDBObject();
		condition.put(Organization.F__ID,
				new BasicDBObject().append("$in", orgIds));
		condition.put(Organization.F_IS_FUNCTION_DEPARTMENT, Boolean.TRUE);
		DBCursor cur = orgCol.find(condition);
		while (cur.hasNext()) {
			Organization org = ModelService.createModelObject(cur.next(),
					Organization.class);
			if(!orgs.contains(org)){
				orgs.add(org);
			}
		}
	}


	/**
	 * ���ڷ�ҳ��ѯʱԤ��ȫ����ҳ��
	 */
	@Override
	public long getTotalCount() {
		return count;
	}

}
