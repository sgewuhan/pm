package com.sg.business.model.commonlabel;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.AccountInfo;
import com.sg.business.model.Document;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class DocumentCommonHTMLLable extends CommonHTMLLabel {

	private Document doc;

	public DocumentCommonHTMLLable(Document document) {
		this.doc =document;
	}
	
	@Override
	public String getHTML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$

		// 显示文档图标
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(doc.getTypeIconURL());
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />"); //$NON-NLS-1$

		if (doc.isLocked()) {
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_LOCK_16,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='position:absolute; left:14; bottom:4; display:block;' width='16' height='16' />"); //$NON-NLS-1$
		}

		// 显示文件名称
		String desc = doc.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(desc);
		String docNum = doc.getDocumentNumber();
		if (!Utils.isNullOrEmpty(docNum)) {
			sb.append("|"); //$NON-NLS-1$
			sb.append(docNum);
		}

		// 显示版本
		String rev = doc.getRevId();
		sb.append(" <b>"); //$NON-NLS-1$
		sb.append("Rev:"); //$NON-NLS-1$
		sb.append(rev);
		sb.append("</b>"); //$NON-NLS-1$


		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		// 显示创建时间，创建人
		// Date date = doc.get_cdate();
		// sb.append(String.format(Utils.FORMATE_DATE_FULL, date));

		AccountInfo ca = doc.get_caccount();
		if (ca != null) {

			sb.append(" "); //$NON-NLS-1$
			sb.append(ca.getUserName());
		}

		// 显示状态
		String status = doc.getLifecycleName();
		sb.append(" <span style='color:rgb(0,128,0)'>"); //$NON-NLS-1$
		sb.append(status);

		if (doc.isLocked()) {
			sb.append(Messages.get(getLocale()).UserTaskDocumentLabelProvider_2);
		}

		sb.append("</span>"); //$NON-NLS-1$

		// String summary = doc.getSummary();
		// if (summary != null) {
		// sb.append(Messages.get().UserTaskDocumentLabelProvider_3);
		// String plainText = Utils.getPlainText(summary);
		// plainText = Utils.getLimitLengthString(plainText, 40);
		// sb.append(plainText);
		// }
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$

		//上传
		if (!doc.isLocked()) {
			sb.append("<a href=\"upload@" + doc.get_id().toString() //$NON-NLS-1$ 
					+ "\" target=\"_rwt\">"); //$NON-NLS-1$
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_ADD_24,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='border-style:none;position:absolute; right:8; bottom:8; display:block;' width='24' height='24' />"); //$NON-NLS-1$
			sb.append("</a>");
		}

		//下载
		sb.append("<a href=\"downloadall@" + doc.get_id().toString() //$NON-NLS-1$ 
				+ "\" target=\"_rwt\">"); //$NON-NLS-1$
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_DOWN_24,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;position:absolute; right:40; bottom:8; display:block;' width='24' height='24' />"); //$NON-NLS-1$
		sb.append("</a>");
		return sb.toString();
	}

}
