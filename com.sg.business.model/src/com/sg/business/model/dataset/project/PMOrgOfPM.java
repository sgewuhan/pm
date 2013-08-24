package com.sg.business.model.dataset.project;

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
 * �̳���{@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory},
 * ���ڻ����Ŀ�����ż�����Ϣ��Ӧ�����½���Ŀ��ѡ����Ŀ������<br/>
 * �������¹��ܣ�
 * <li>��ȡ��Ŀ�����ż���
 * <li>��ȡ��ѡ��Ŀ��Ŀ����������֯���¼�������Ŀ����ְ�ܵ���֯
 * <li>��ȡ��ѡ��Ŀ��Ŀ����������֯���ϼ�������Ŀ����ְ�ܵ���֯
 * 
 * @author yangjun
 * 
 */
public class PMOrgOfPM extends MasterDetailDataSetFactory {

	/**
	 * ��Ŀ�����ż��ϵĹ��캯��,������Ŀ�������ڵ���Ŀ�����Ŵ������ݿ⼰���ݴ洢��
	 */
	public PMOrgOfPM() {
		//
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
			Project project = (Project) master;
			// �����Ŀ����
			User charger = project.getCharger();
			// �����Ŀ������������֯
			Organization org = charger.getOrganization();
			searchUp(org, list);
			searchDown(org, list);
		}
		return new DataSet(list);
	}

	/**
	 * ��ȡ��ѡ��Ŀ��Ŀ����������֯���¼�������Ŀ����ְ�ܵ���֯��
	 * ����ǰ��֯��������������Ŀ����ְ�ܵ���֯ѭ���ݹ���ӵ�List<{@link PrimaryObject}>
	 * 
	 * @param org : ��ǰ��֯
	 * @param list �� ��Ŀ�����ż���
	 */
	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			//�жϵ�ǰ��֯�Ƿ��Ǿ�����Ŀ����ְ�ܵ���֯��������ǡ�����ӵ���Ӧ��List<PrimaryObject>��
			if (child.isFunctionDepartment()) {
				list.add(child);
			}
			searchDown(child, list);
		}
	}

	/**
	 * ��ȡ��ѡ��Ŀ��Ŀ����������֯���ϼ�������Ŀ����ְ�ܵ���֯��
	 * ����ǰ��֯�����ϼ�������Ŀ����ְ�ܵ���֯ѭ���ݹ���ӵ�List<{@link PrimaryObject}>
	 * 
	 * @param org �� ��ǰ��֯
	 * @param list �� ��Ŀ�����ż���
	 */
	private void searchUp(Organization org, List<PrimaryObject> list) {
		//�жϵ�ǰ��֯�Ƿ��Ǿ�����Ŀ����ְ�ܵ���֯��������ǡ�����ӵ���Ӧ��List<PrimaryObject>��
		if (org.isFunctionDepartment()) {
			list.add(0, org);
		}
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
