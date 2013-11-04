package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.AccountInfo;
import com.sg.business.model.Document;

public class UserTaskDocumentLabelProvider extends ColumnLabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof Document) {
			Document doc = (Document) element;
			return getDocumentText(doc);
		} else if(element instanceof RemoteFile){
			return element.toString();
		}
		
		return "";

	}

	private String getDocumentText(Document doc) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		sb.append("<span style='float:right;padding-right:4px'>");
		AccountInfo ca = doc.get_caccount();
		sb.append(ca.getUserName());
		sb.append("|");
		sb.append(ca.getUserId());
		sb.append("</span>");

		// 显示文档图标
		sb.append("<img src='");
		sb.append(doc.getTypeIconURL());
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />");

		// 显示文件名称
		String desc = doc.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(desc);
		String docNum = doc.getDocumentNumber();
		sb.append("|");
		sb.append(docNum);

		// 显示版本
		String rev = doc.getRevId();
		sb.append(" Rev:");
		sb.append(rev);
		
		// 显示状态
		String status = doc.getLifecycleName();
		sb.append(" ");
		sb.append(status);

		
		sb.append("<br/>");
		sb.append("<small>");
		// 显示创建时间，创建人
		Date date = doc.get_cdate();
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(String.format(Utils.FORMATE_DATE_FULL, date));
		sb.append("</span>");

		String summary = doc.getSummary();
		if (summary != null) {
			sb.append("摘要: ");
			String plainText = Utils.getPlainText(summary);
			plainText = Utils.getLimitLengthString(plainText, 40);
			sb.append(plainText);
		}
		sb.append("</small>");

		sb.append("</span>");
		return sb.toString();
	}

}
