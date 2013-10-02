package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��Ŀ�����ż���
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ����Ŀ�����ŵļ�����Ϣ��Ӧ�����½���Ŀ��ѡ����Ŀ������<br/>
 * �������¹��ܣ�
 * <li>��ȡ��Ŀ�����ż���
 * <li>��ȡ��ѡ��Ŀ��Ŀ����������֯���¼���֯
 * <li>��ȡ��ѡ��Ŀ��Ŀ����������֯���ϼ���֯
 * 
 * @author yangjun
 * 
 */
public class LaunchOrgOfPM extends MasterDetailDataSetFactory {

	/**
	 * ��Ŀ�����ż��ϵĹ��캯��,������Ŀ�������ڵ���Ŀ�����Ŵ������ݿ⼰���ݴ洢��
	 */
	public LaunchOrgOfPM() {
		//������Ŀ�������ڵ���Ŀ�����Ŵ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * ��ȡ��Ŀ�����ż���
	 * @return ��Ŀ�������ڵ���Ŀ�����ż��ϵ����ݼ�
	 */
	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> list = new ArrayList<PrimaryObject>();
		if (master != null) {
			//��ȡ��ǰ��ѡ��Ŀ
			Project project = (Project) master;
			// ��õ�ǰ��ѡ��Ŀ����Ŀ����
			User charger = project.getCharger();
			// ��õ�ǰ��ѡ��Ŀ����Ŀ������������֯
			Organization org = charger.getOrganization();
			searchUp(org, list);
			searchDown(org, list);
		}
		return new DataSet(list);
	}

	/**
	 * ��ȡ��ѡ��Ŀ��Ŀ����������֯���¼���֯��
	 * ����ǰ��֯����������֯ѭ���ݹ���ӵ�List<{@link PrimaryObject}>
	 * @param org : ��ǰ��֯
	 * @param list �� ��Ŀ�����ż���
	 */
	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			searchDown(child, list);
		}
	}

	/**
	 * ��ȡ��ѡ��Ŀ��Ŀ����������֯���ϼ���֯��
	 * ����ǰ��֯�����ϼ���֯ѭ���ݹ���ӵ�List<{@link PrimaryObject}>
	 * 
	 * @param org �� ��ǰ��֯
	 * @param list �� ��Ŀ�����ż���
	 */
	private void searchUp(Organization org, List<PrimaryObject> list) {
		list.add(0, org);
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	/**
	 * ������Ŀ�����ż��ϵĹ����ֶΣ�{@link NULL}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return null;
	}
}
