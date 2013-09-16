package com.sg.business.commons.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

/**
 * ������ǩ
 * @author gdiyang
 *
 */
public class AttachmentLabelProvider extends ConfiguratorColumnLabelProvider {

	/**
	 * ���ø�����ʾ��ǩ
	 */
	@Override
	public Image getImage(Object element) {
 		PrimaryObject po = (PrimaryObject)element;
 		/******
 		 * @author gdiyang
 		 * 
 		 * �޸�getValue��ȡֵ�ķ�ʽΪgetText�ķ�ʽ���������ȡ��ϢΪ""ʱ���ж�����
 		 * 
 		 */
		String key = getFieldName();
		//�ж��Ƿ���ڸ���
		if(!"".equals(po.getText(key))){
			return BusinessResource.getImage(BusinessResource.IMAGE_ATTACHMENT_32);
		}else {
			return null;
		}
		
	}

	@Override
	public String getText(Object element) {
		return "";
	}

}
