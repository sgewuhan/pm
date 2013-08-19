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
 * �����Ŀ�������ڵ���Ŀ������
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class LaunchOrgOfPM extends MasterDetailDataSetFactory {

	/**
	 * ��Ŀ�������ڵ���Ŀ�����Ź��캯��
	 */
	public LaunchOrgOfPM() {
		//������Ŀ�������ڵ���Ŀ�����Ŵ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * ��ȡ��Ŀ�������ڵ���Ŀ�����ż���
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

	private void searchDown(Organization org, List<PrimaryObject> list) {
		List<PrimaryObject> children = org.getChildrenOrganization();
		for (int i = 0; i < children.size(); i++) {
			Organization child = (Organization) children.get(i);
			searchDown(child, list);
		}
	}

	private void searchUp(Organization org, List<PrimaryObject> list) {
		list.add(0, org);
		Organization parent = (Organization) org.getParentOrganization();
		if (parent != null) {
			searchUp(parent, list);
		}
	}

	/**
	 * ������Ŀ�������ڵ���Ŀ�����ŵ�MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return null;
	}
}
