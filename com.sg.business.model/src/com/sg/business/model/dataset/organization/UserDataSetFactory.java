package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

/**
 * <p>
 * �û�����
 * </p>
 * ��ʾ��ǰϵͳ�е��˺���Ϣ
 * <br/>
 * �̳��� {@link com.sg.widgets.commons.dataset.OptionDataSetFactory}
 * 
 * @author gdiyang
 *
 */
public class UserDataSetFactory extends SingleDBCollectionDataSetFactory {

	/**
	 * �û����Ϲ��캯��
	 */
	public UserDataSetFactory() {
		//�����û����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_USER);
	}
}
