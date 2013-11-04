package com.sg.business.work.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.file.FileUtil;
import com.sg.business.model.Document;
import com.sg.business.resource.BusinessResource;

public class UserTaskDocumentLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		Document doc = (Document) element;
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:9pt'>");
		// ÏÔÊ¾ÎÄµµÍ¼±ê
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_DOCUMENT_24,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;float:right;padding:0px;margin:0px' width='24' height='24' />");

		sb.append("<br/>");
		sb.append("<small>");
		sb.append("</small>");

		sb.append("</span>");
		return sb.toString();
	}

}
