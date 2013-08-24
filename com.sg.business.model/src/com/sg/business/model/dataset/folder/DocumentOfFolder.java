package com.sg.business.model.dataset.folder;

import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * �ĵ�����
 * </p>
 * �ĵ��������������ĵ����е��ĵ���Ϣ<br/>
 * �̳��� {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * ���ڻ�ȡ��֯���µ��ĵ���Ϣ<br/>
 * ʵ�����¼��ֹ��ܣ�
 * <li>��ȡ�ĵ�����������Ϣ
 * <li>ͨ��FOLDERID�������ĵ�����������Ϊ���ǡ�����֯
 * 
 * @author yangjun
 * 
 */
public class DocumentOfFolder extends MasterDetailDataSetFactory {

	/**
	 * �ĵ����Ϲ��캯��,���������ĵ����ϵĴ�����ݿ⼰���ݴ洢��
	 */
	public DocumentOfFolder() {
		//�����ĵ����ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	/**
	 * �����ĵ���������֯�Ĺ����ֶΣ�{@link com.sg.business.model.Document.F_FOLDER_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Document.F_FOLDER_ID;
	}
}
