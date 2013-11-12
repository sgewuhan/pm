package com.sg.business.model.dataset.folder;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.Container;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * ��Ȩ�ĵ���
 * </p>
 * �̳��� {@link com.mobnut.db.model.DataSetFactory}����õ�ǰ�û���Ȩ�鿴�������ĵ�������Ȩ�ޣ�����֯���ĵ���
 * ��������Ȩ�鿴����֯���ļ���
 * <br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��Ȩ�鿴����֯���ļ�����
 * <li>��ȡ��Ȩ�鿴����֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
 * 
 * @author yangjun
 * 
 */
public class VaultOfGuestRole extends DataSetFactory {
	
	/**
	 * ��ǰ�û���Ȩ�鿴����֯���ļ�����
	 */
	private long count;

	/**
	 * ��ȡ��ǰ�û���Ȩ�鿴����֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * @param ds : ��֯�������ݼ�
	 * @return ʵ������{@link com.mobnut.db.model.PrimaryObject}����
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		User currentUser = UserToolkit.getUserById(userId);
		
		// �ڵ�ǰ�û����ڵ���֯�Լ��¼���֯�л�ȡ"������"����֯
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();

		// ������衰�ĵ������ߡ���ɫ����֯����
		addRoleGrantedOrganizationContainer(currentUser,
				Role.ROLE_VAULT_GUEST_ID,Container.TYPE_GUEST_GRANTED, containers);

		//��ȡ��ǰ�û��ɷ��ʵ���֯����������
		count = containers.size();
		return containers;
	}

	/**
	 * �����Ȩ�鿴����֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * @param user �� ��¼�û�����Ϣ
	 * @param roleNumber : ��ɫ���
	 * @param containerType �� ��Ȩ��Ϣ,��Ϣ��Դ��{@link com.sg.business.model.Container}
	 * @param containers �� ��֯��������
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
		
		//����ѯ������֯���䵽��֯����������
		DBCursor cur = orgCol.find(condition);
		while (cur.hasNext()) {
			Organization org = ModelService.createModelObject(cur.next(),
					Organization.class);
			//���⻯����֯����
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
