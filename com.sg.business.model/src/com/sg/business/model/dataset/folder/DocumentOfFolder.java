package com.sg.business.model.dataset.folder;

import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * �ĵ�����
 * <p/>
 * �ĵ��������������ĵ����е��ĵ���Ϣ<br/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class DocumentOfFolder extends MasterDetailDataSetFactory {

	/**
	 * �ĵ����Ϲ��캯��
	 */
	public DocumentOfFolder() {
		//�����ĵ����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	/**
	 * �����ĵ����ϵ�MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Document.F_FOLDER_ID;
	}
}
