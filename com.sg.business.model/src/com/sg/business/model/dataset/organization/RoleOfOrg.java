package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��֯���µĽ�ɫ����
 * <p/>
 * ��֯���µĽ�ɫ�������ڻ�ȡ��ǰ��֯���µĽ�ɫ����
 * <br/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class RoleOfOrg extends MasterDetailDataSetFactory {

	/**
	 * ��֯���µĽ�ɫ���Ϲ��캯��
	 */
	public RoleOfOrg() {
		//���ý�ɫ���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	/**
	 * ������֯���µĽ�ɫ���ϵ�MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
