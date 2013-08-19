package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

/**
 * <p>
 * ��֯����
 * <p/>
 * ��֯�������ڻ�ȡ��ǰ��֯����
 * <br/>
 * �̳��� {@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class OrgRoot extends SingleDBCollectionDataSetFactory {

	/**
	 * ��֯���Ϲ��캯��
	 */
	public OrgRoot() {
		//������֯���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		//���ò�ѯ����
		setQueryCondition(new BasicDBObject().append(Organization.F_PARENT_ID, null));
	}


	
}
