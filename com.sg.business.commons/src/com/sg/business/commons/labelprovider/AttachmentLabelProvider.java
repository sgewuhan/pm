package com.sg.business.commons.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.mobnut.db.model.PrimaryObject;
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
		PrimaryObject po = (PrimaryObject)element;
		String key = getFieldName();
		//判断是否存在附件
		if(po.getValue(key) != null){
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
