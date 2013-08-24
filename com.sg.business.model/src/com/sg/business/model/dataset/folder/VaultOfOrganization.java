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
 * <p>
 * ��֯�ĵ���
 * </p>
 * �̳��� {@link com.mobnut.db.model.DataSetFactory}����õ�ǰ�û�������֯���ĵ���
 * ������������֯�����¼���֯���ļ���
 * <br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ������֯�����¼���֯���ļ�����
 * <li>��ȡ������֯�����¼���֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
 * 
 * @author yangjun
 * 
 */
public class VaultOfOrganization extends DataSetFactory {

	/**
	 * ��ǰ�û�������֯�����¼���֯���ļ�����
	 */
	private long count;

	/**
	 * ��ȡ������֯�����¼���֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * @param ds : ��֯�������ݼ�
	 * @return ʵ������List<{@link com.mobnut.db.model.PrimaryObject}>����
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		
		// �ӵ�ǰ�Ľ����л�õ�¼�û�����Ϣ
		ObjectId useroid = UserSessionContext.getSession().getUserOId();
		User currentUser = ModelService.createModelObject(User.class, useroid);
		
		// �ڵ�ǰ�û����ڵ���֯�Լ��¼���֯�л�ȡ"������"����֯
		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();
		
		// ����¼�����֯����
		addSubOrganizationContainer(org, containers);

		//��ȡ��ǰ�û��ɷ��ʵ���֯����������
		count = containers.size();
		return containers;
	}


	/**
	 * �ݹ����������֯�����¼���֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * 
	 * @param startOrganization : ��ʼ��֯
	 * @param containers �� ��֯��������
	 */
	private void addSubOrganizationContainer(Organization startOrganization,
			List<PrimaryObject> containers) {
		//�ж���ʼ��֯�Ƿ�Ϊ��������֯�����Ϊ��������֯������ӵ�OrganizationList��
		if (Boolean.TRUE.equals(startOrganization.isContainer())) {
			Container container = Container.adapter(startOrganization,
					Container.TYPE_OWNER);
			if(!containers.contains(container)){
				containers.add(container);
			}
		}
		//��ȡ��ʼ��֯���¼���֯���ݹ���ã�������������֯��ӵ�OrganizationList��
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
