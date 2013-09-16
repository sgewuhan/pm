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
 		/******
 		 * @author gdiyang
 		 * 
 		 * 修改getValue获取值的方式为getText的方式来解决当获取信息为""时的判断问题
 		 * 
 		 */
		String key = getFieldName();
		//判断是否存在附件
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
