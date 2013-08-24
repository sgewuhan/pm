package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��֯����
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡ��֯������Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��֯����������Ϣ
 * <li>ͨ��id������ѡ��֯
 * 
 * @author yangjun
 * 
 */
public class OrgMaster extends MasterDetailDataSetFactory {

	/**
	 * ��֯���Ϲ��캯��,����������֯���ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public OrgMaster() {
		//������֯���ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * ������֯��������֯�Ĺ����ֶΣ�{@link com.sg.business.model.Organization.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Organization.F__ID;
	}

}
