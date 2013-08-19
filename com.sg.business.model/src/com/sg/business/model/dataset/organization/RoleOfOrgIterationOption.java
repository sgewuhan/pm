package com.sg.business.model.dataset.organization;

import java.util.List;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * ��������Ľ�ɫ����
 * </p>
 * ����ͨ�ù�������Ͷ����������� �����๤������ֱ��ʹ������֯�Ľ�ɫ������������ɫ���� ��ȡѡ��ʱ��
 * ��Ҫ���ظ�ְ�ܲ��ż��¼������н�ɫ ����಻��֤�̰߳�ȫ
 * <br/>
 * �̳���{@link com.sg.widgets.commons.dataset.OptionDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class RoleOfOrgIterationOption extends OptionDataSetFactory {

	private Organization organization;

	/**
	 * ��������Ľ�ɫ���Ϲ��캯��
	 */
	public RoleOfOrgIterationOption() {
		//���ý�ɫ���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	/**
	 * ���õ�ǰ�����������֯
	 * @param data : ��ǰ��������
	 */
	@Override
	public void setEditorData(PrimaryObject data) {
		// ��ȡ��ǰ��������
		WorkDefinition workd = (WorkDefinition) data;
		// ��õ�ǰ��֯
		organization = workd.getOrganization();
	}

	/**
	 * ��ȡ��ǰ��֯���¼���֯�Ľ�ɫ����
	 * @return ��ǰ��֯���¼���֯�Ľ�ɫ���ϵ����ݼ�
	 */
	@Override
	public DataSet getDataSet() {
		// ��ȡ�����Լ��¼����еĽ�ɫ
		List<PrimaryObject> dataItems = organization.getRolesIteration();
		return new DataSet(dataItems);
	}



}
