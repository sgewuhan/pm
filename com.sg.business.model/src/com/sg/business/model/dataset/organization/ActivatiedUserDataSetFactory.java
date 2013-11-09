package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.User;

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
public class ActivatiedUserDataSetFactory extends SingleDBCollectionDataSetFactory {

	/**
	 * �û����Ϲ��캯��,���������û����ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public ActivatiedUserDataSetFactory() {
		//�����û����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_USER);
		setQueryCondition(new BasicDBObject().append(User.F_ACTIVATED, Boolean.TRUE));
	}
}
