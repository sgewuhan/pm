package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

/**
 * <p>
 * ������֯����
 * <p/>
 * �̳��� {@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}��
 * ���ڻ�ȡ���ж�����֯���µ��ĵ���Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ���ж�����֯����������Ϣ,����˵�����{@link com.sg.business.model.Organization.F_PARENT_ID}��Ϊ�յ���֯����
 * 
 * @author yangjun
 * 
 */
public class OrgRoot extends SingleDBCollectionDataSetFactory {

	/**
	 * ������֯���Ϲ��캯��,���������ĵ����ϵĴ�����ݿ⼰���ݴ洢��
	 * <br/>
	 * ����˵�����{@link com.sg.business.model.Organization.F_PARENT_ID}��Ϊ�յ���֯����
	 */
	public OrgRoot() {
		//������֯���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		//���ò�ѯ����
		setQueryCondition(new BasicDBObject().append(Organization.F_PARENT_ID, null));
	}


	
}
