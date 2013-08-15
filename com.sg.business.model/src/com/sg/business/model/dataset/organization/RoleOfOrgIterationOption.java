package com.sg.business.model.dataset.organization;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * ����ͨ�ù�������Ͷ����������� �����๤������ֱ��ʹ������֯�Ľ�ɫ������������ɫ���� ��ȡѡ��ʱ����Ҫ���ظ�ְ�ܲ��ż��¼������н�ɫ ����಻��֤�̰߳�ȫ
 * 
 * @author Administrator
 * 
 */
public class RoleOfOrgIterationOption extends OptionDataSetFactory {

	private Organization organization;

	public RoleOfOrgIterationOption() {
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	@Override
	public void setEditorData(PrimaryObject data) {
		WorkDefinition workd = (WorkDefinition) data;
		// ���ְ�ܲ���
		organization = workd.getOrganization();
	}

	@Override
	public DataSet getDataSet() {
		List<PrimaryObject> dataItems = organization.getRolesIteration();
		return new DataSet(dataItems);
	}



}
