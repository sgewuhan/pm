package com.sg.business.model;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;

/**
 * ������<p/>
 * ���������빤����ɺ�������ĵ�
 * @author jinxitao
 *
 */
public class Deliverable extends PrimaryObject implements IProjectRelative{

	/**
	 * ����_id�ֶΣ����ڱ��湤��_id��ֵ
	 */
	public static final String F_WORK_ID = "work_id";
	
	/**
	 * 
	 */
	public static final String F_MANDATORY = "mandatory";
	
	/**
	 * �ĵ�_id�ֶΣ����ڱ����ĵ�_id��ֵ
	 */
	public static final String F_DOCUMENT_ID = "document_id";
	
	/**
	 * ������ı༭��
	 */
	public static final String EDITOR = null;

	/**
	 * ������ʾͼ��
	 */
	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_DELIVERABLE_16);
	}
	
	@Override
	public String getTypeName() {
		return "������";
	}

	@Override
	public String getDefaultEditorId() {
		return EDITOR;
	}
}
