package com.sg.business.commons.labelprovider;

import java.util.List;

import org.eclipse.swt.graphics.Image;

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
		//�������ļ����ֶ��� List���ͣ���List��Ϊ��
		
		Object value = getValue(element);
		if(value instanceof List<?> && !((List<?>)value).isEmpty()){
			return BusinessResource.getImage(BusinessResource.IMAGE_ATTACHMENT_24);
		}
		return null;
		
	}

	@Override
	public String getText(Object element) {
		return ""; //$NON-NLS-1$
	}

}
