package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * �ĵ�ģ�弯��
 * <p/> 
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}��
 * ���ڻ�ȡҵ���������ѡ��֯���ĵ�ģ�弯����Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ�ĵ�ģ�弯��������Ϣ
 * <li>ͨ��F_ORGANIZATION_ID������ѡ��֯
 * 
 * @author yangjun
 *
 */
public class DocDefOfOrg extends
		MasterDetailDataSetFactory {

	/**
	 * ��ȡ�ĵ�ģ�弯��������Ϣ,���������ĵ�ģ�漯�ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public DocDefOfOrg() {
		//���������ĵ�ģ�漯�ϵĴ�����ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT_DEFINITION);
	}

	/**
	 * �����ĵ�ģ�弯������֯�Ĺ����ֶΣ�{@link com.sg.business.model.DocumentDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return DocumentDefinition.F_ORGANIZATION_ID;
	}

}
