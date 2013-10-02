package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * <p>
 * ��ǰ�û����ϼ���֯���¼���֯
 * </p>
 * 
 */
public class LaunchOrgOfCurrentUser extends SingleDBCollectionDataSetFactory {

	public LaunchOrgOfCurrentUser() {
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * ��ȡ��Ŀ�����ż���
	 * 
	 * @return ��Ŀ�������ڵ���Ŀ�����ż��ϵ����ݼ�
	 */
	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		String userId = new CurrentAccountContext().getAccountInfo().getConsignerId();
		User user = UserToolkit.getUserById(userId);
		
		Organization org = user.getOrganization();
		searchUp(org, list);
		searchDown(org, list);
		return new DataSet(list);
	}

	/**
	 * ��ȡ��ѡ��Ŀ��Ŀ����������֯���¼���֯�� ����ǰ��֯����������֯ѭ���ݹ���ӵ�List<{@link PrimaryObject}>
	 * 
	 * @param org
	 *            : ��ǰ��֯
	 * @param list
	 *            �� ��Ŀ�����ż���
	 */
	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			searchDown(child, list);
		}
	}

	/**
	 * ��ȡ��ѡ��Ŀ��Ŀ����������֯���ϼ���֯�� ����ǰ��֯�����ϼ���֯ѭ���ݹ���ӵ�List<{@link PrimaryObject}>
	 * 
	 * @param org
	 *            �� ��ǰ��֯
	 * @param list
	 *            �� ��Ŀ�����ż���
	 */
	private void searchUp(Organization org, List<PrimaryObject> list) {
		list.add(0, org);
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}
}
