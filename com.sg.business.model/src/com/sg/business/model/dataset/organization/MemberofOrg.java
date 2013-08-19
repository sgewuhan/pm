package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * ��֯��Ա����
 * <p/>
 * ��֯��Ա�������ڻ�ȡ��ǰ��֯�ĳ�Ա����
 * <br/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class MemberofOrg extends MasterDetailDataSetFactory {
	
	/**
	 * ��֯��Ա���Ϲ��캯��
	 */
	public MemberofOrg() {
		//������֯��Ա���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	/**
	 * ������֯��Ա���ϵ�MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}
}
