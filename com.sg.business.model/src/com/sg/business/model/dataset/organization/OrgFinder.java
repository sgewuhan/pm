package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * �ϼ���֯����
 * <p/>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}���ڻ�ȡ��֯���µ��ĵ���Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ�ϼ���֯����������Ϣ
 * 
 * @author yangjun
 * 
 */
public class OrgFinder extends OptionDataSetFactory {

	/**
	 * �ϼ���֯���Ϲ��캯��,���������ϼ���֯���ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public OrgFinder() {
		//������֯���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}


	
}
