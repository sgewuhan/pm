package com.sg.business.model.dataset.folder;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.Container;
import com.sg.business.model.Organization;
import com.sg.business.model.User;

/**
 * ��õ�ǰ�û��Ŀ���Ŀ¼ ������������֯���ļ��У��������Ŀ���ļ���
 * 
 * @author Administrator
 * 
 */
public class VaultOfOrganization extends DataSetFactory {

	private long count;

	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		// �ڵ�ǰ�û����ڵ���֯�Լ��¼���֯�л�ȡ"������"����֯
		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();
		
//		// ������衰�ĵ�����Ա����ɫ����֯����
//		addRoleGrantedOrganizationContainer(currentUser,
//				Role.ROLE_VAULT_ADMIN_ID,Container.TYPE_ADMIN_GRANTED, containers);
		
		// ����¼�����֯����
		addSubOrganizationContainer(org, containers);
		
//		// ������衰�ĵ������ߡ���ɫ����֯����
//		addRoleGrantedOrganizationContainer(currentUser,
//				Role.ROLE_VAULT_GUEST_ID,Container.TYPE_GUEST_GRANTED, containers);

		count = containers.size();
		return containers;
	}


	/**
	 * ����¼�����֯���� ����ʼ��֯�Լ�����֯���¼���֯���������������֯����ӵ�orgList��
	 * 
	 * @param startOrganization
	 *            ��ʼ��֯
	 * @param containers
	 */
	private void addSubOrganizationContainer(Organization startOrganization,
			List<PrimaryObject> containers) {
		if (Boolean.TRUE.equals(startOrganization.isContainer())) {
			Container container = Container.adapter(startOrganization,
					Container.TYPE_OWNER);
			if(!containers.contains(container)){
				containers.add(container);
			}
		}
		List<PrimaryObject> children = startOrganization
				.getChildrenOrganization();
		for (PrimaryObject child : children) {
			addSubOrganizationContainer((Organization) child, containers);
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
