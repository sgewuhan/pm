package com.sg.business.model.dataset.folder;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.DataSetFactory;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Container;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class VaultOfOpened extends DataSetFactory {
	private User currentUser;

	public VaultOfOpened() {
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		currentUser = UserToolkit.getUserById(userId);
	}

	public VaultOfOpened(String userId) {
		currentUser = UserToolkit.getUserById(userId);
	}

	/**
	 * ��ǰ�û�������֯�����¼���֯���ļ�����
	 */
	private long count;

	/**
	 * ��ȡ������֯�����ϼ���֯���ļ��е�List<{@link com.mobnut.db.model.PrimaryObject}>����
	 * 
	 * @param ds
	 *            : ��֯�������ݼ�
	 * @return ʵ������List<{@link com.mobnut.db.model.PrimaryObject}>����
	 */
	@Override
	public List<PrimaryObject> doQuery(DataSet ds) throws Exception {
		// �ڵ�ǰ�û����ڵ���֯�Լ��¼���֯�л�ȡ"������"����֯
		Organization org = currentUser.getOrganization();
		List<PrimaryObject> containers = new ArrayList<PrimaryObject>();

		addParentOrganizationContainer(org, containers);

		// ��ȡ��ǰ�û��ɷ��ʵ���֯����������
		count = containers.size();
		return containers;
	}

	private void addParentOrganizationContainer(Organization startOrganization,
			List<PrimaryObject> containers) {
		Organization parentOrg = (Organization) startOrganization
				.getParentOrganization();
		if (parentOrg != null) {
			addParentOrganizationContainer(parentOrg, containers);

			// �ж���ʼ��֯���ϼ���֯�з�Ϊ��������֯�����Ϊ��������֯���������Ƿ���ڿ������Ե��ļ��У������������ӵ�OrganizationList��
			if (Boolean.TRUE.equals(parentOrg.isContainer())) {
				Container container = Container.adapter(parentOrg,
						Container.TYPE_OWNER);
				long count = parentOrg.getOpenedFolderCount();
				if (count > 0) {
					if (!containers.contains(container)) {
						containers.add(container);
					}
				}
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
