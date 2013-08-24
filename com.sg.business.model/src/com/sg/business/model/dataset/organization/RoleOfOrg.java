package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��֯���µĽ�ɫ����
 * <p/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * �����ѡ��֯���µĽ�ɫ����
 * <br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��ɫ����������Ϣ
 * <li>ͨ��ORGANIZATIONID������ѡ����֯
 * @author yangjun
 * 
 */
public class RoleOfOrg extends MasterDetailDataSetFactory {

	/**
	 * ��֯���µĽ�ɫ���Ϲ��캯��,�������ý�ɫ���ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public RoleOfOrg() {
		//���ý�ɫ���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	/**
	 * ���ý�ɫ��������֯�Ĺ����ֶΣ�{@link com.sg.business.model.Role.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
