package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.OptionDataSetFactory;

/**
 * <p>
 * ��֯����
 * <p/>
 * ��֯�������ڻ�ȡ��ǰ��֯���ϣ����ڱ༭����ʹ��
 * <br/>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class OrgFinder extends OptionDataSetFactory {

	/**
	 * ��֯���Ϲ��캯��
	 */
	public OrgFinder() {
		//������֯���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}


	
}
