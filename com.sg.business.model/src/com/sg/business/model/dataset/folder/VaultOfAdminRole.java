package com.sg.business.model.dataset.folder;

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
import com.sg.business.model.Container;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;

/**
 * ��õ�ǰ�û��Ŀ���Ŀ¼ ������������֯���ļ��У��������Ŀ���ļ���
 * 
 * @author Administrator
 * 
 */
public class VaultOfAdminRole extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		// �ڵ�ǰ�û����ڵ���֯�Լ��¼���֯�л�ȡ"������"����֯
//		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();
		
		// ������衰�ĵ�����Ա����ɫ����֯����
		addRoleGrantedOrganizationContainer(currentUser,
				Role.ROLE_VAULT_ADMIN_ID,Container.TYPE_ADMIN_GRANTED, containers);
		
//		// ����¼�����֯����
//		addSubOrganizationContainer(org, containers);
//		
//		// ������衰�ĵ������ߡ���ɫ����֯����
//		addRoleGrantedOrganizationContainer(currentUser,
//				Role.ROLE_VAULT_GUEST_ID,Container.TYPE_GUEST_GRANTED, containers);

		count = containers.size();
		return containers;
	}

	/**
	 * @param user
	 * @param containers
	 * @param containerType
	 * @param roleId
	 */
	private void addRoleGrantedOrganizationContainer(User user,
			String roleNumber, int containerType, List<PrimaryObject> containers) {
		// ����û��������roleNumber��Ӧ�Ľ�ɫ
		List<PrimaryObject> roles = user.getRoles(roleNumber);

		// ȡ����Щ��ɫ��������֯��id
		ObjectId[] orgIds = new ObjectId[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			orgIds[i] = ((Role) roles.get(i)).getOrganization_id();
		}

		// ��ѯ������������֯
		DBCollection orgCol = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_ORGANIZATION);
		DBObject condition = new BasicDBObject();
		condition.put(Organization.F__ID,
				new BasicDBObject().append("$in", orgIds));
		condition.put(Organization.F_IS_CONTAINER, Boolean.TRUE);
		DBCursor cur = orgCol.find(condition);
		while (cur.hasNext()) {
			Organization org = ModelService.createModelObject(cur.next(),
					Organization.class);
			Container container = Container.adapter(org, containerType);
			if(!containers.contains(container)){
				containers.add(container);
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
