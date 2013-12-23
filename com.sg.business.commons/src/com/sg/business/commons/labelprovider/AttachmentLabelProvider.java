package com.sg.business.commons.labelprovider;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

/**
 * 附件标签
 * @author gdiyang
 *
 */
public class AttachmentLabelProvider extends ConfiguratorColumnLabelProvider {

	/**
	 * 设置附件显示标签
	 */
	@Override
	public Image getImage(Object element) {
		//保存了文件的字段是 List类型，且List不为空
		
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
