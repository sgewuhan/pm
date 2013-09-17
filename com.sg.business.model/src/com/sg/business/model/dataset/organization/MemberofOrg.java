package com.sg.business.model.dataset.organization;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��֯��Ա����
 * <p/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡ��ǰ��֯�ĳ�Ա������Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��֯��Ա����������Ϣ
 * <li>ͨ��organizationid������ѡ��֯
 * 
 * @author yangjun
 * 
 */
public class MemberofOrg extends MasterDetailDataSetFactory {

	/**
	 * ��֯��Ա���Ϲ��캯��,����������֯��Ա���ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public MemberofOrg() {
		// ������֯��Ա���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	/**
	 * ������֯��Ա��������֯�Ĺ����ֶΣ�{@link com.sg.business.model.Role.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
