package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

/**
 * <p>
 * �û�����
 * </p>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}��
 * ��ʾ��ǰϵͳ�е��˺���Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ��ǰϵͳ�е��˺���Ϣ
 * 
 * @author gdiyang
 *
 */
public class UserDataSetFactory extends SingleDBCollectionDataSetFactory {

	/**
	 * �û����Ϲ��캯��,���������û����ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public UserDataSetFactory() {
		//�����û����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_USER);
	}
}
