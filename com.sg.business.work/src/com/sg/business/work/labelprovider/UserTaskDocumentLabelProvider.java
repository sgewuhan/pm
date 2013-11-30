package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.commons.util.file.OSServerFile;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.portal.Portal;
import com.sg.business.model.Document;
import com.sg.business.resource.BusinessResource;

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
		} else if (element instanceof IServerFile) {
			IServerFile serverFile = (IServerFile) element;
			return getRemoteFileText(serverFile);
		}

		return "";

	}

	private String getRemoteFileText(IServerFile serverFile) {
		String fileName = serverFile.getFileName();
		Date uploadDate = serverFile.getUploadDate();
		long length = serverFile.getLength();
		String md5 = serverFile.getMD5();
		// DBObject meta = gsFile.getMetaData();

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		// 显示文档图标
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_FILE_24,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />");

		// 显示文件名称

		// String url = FileUtil.getDownloadUrl(remoteFile.getDbName(),
		// remoteFile.getNamespace(), remoteFile.getObjectId(), fileName);
		// url = url.replaceAll("\\&", "&amp;");

		if (serverFile instanceof OSServerFile) {
			sb.append("<img src='");
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_OUTREP_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' style='position:absolute; left:14; bottom:4; display:block;' width='16' height='16' />");
		}

		if (serverFile instanceof OSServerFile) {
			sb.append("<a href='");
			String downloadURL = ((OSServerFile) serverFile).getDownloadURL();
			sb.append(downloadURL);
			sb.append("'>");
			sb.append(fileName);
			sb.append("</a>");
		}else{
			sb.append("<a href='");
			String downloadURL = serverFile.getInternalDownloadURL();
			sb.append(downloadURL + "@download");
			sb.append("' target=\"_rwt\">");
			sb.append(fileName);
			sb.append("</a>");
			
		}

		sb.append("<br/>");
		// 显示大小
		sb.append("<small>");
		sb.append(" 大小:");
		sb.append(getLength(length));
		sb.append(" 上传时间:");
		sb.append(String.format(Utils.FORMATE_DATE_FULL, uploadDate));
		if (Portal.getDefault().isDevelopMode()) {
			sb.append(" MD5:");
			sb.append(md5);
		}
		sb.append("</small>");

		sb.append("</span>");
		String text = sb.toString();
		return text;
	}

	private String getLength(long length) {
		if (length >= 1000 * 1000 * 1000) {// TB
			double r = length / (1000 * 1000 * 1000d);
			return String.format("%.2f", r) + "TB";
		} else if (length >= 1000 * 1000) {
			double r = length / (1000 * 1000d);
			return String.format("%.2f", r) + "MB";
		} else if (length >= 1000) {
			double r = length / (1000d);
			return String.format("%.2f", r) + "KB";
		} else {
			return length + "Byte";
		}

	}

	private String getDocumentText(Document doc) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		sb.append("<span style='float:right;padding-right:14px'>");

		// 显示状态
		String status = doc.getLifecycleName();
		sb.append(" <span style='color:rgb(0,128,0)'>");
		sb.append(status);

		if (doc.isLocked()) {
			sb.append(", 已锁定");
			// sb.append("[");
			// User lockuser = doc.getLockedBy();
			// if(lockuser!=null){
			// sb.append(lockuser);
			// }
			// Date date = doc.getLockOn();
			// if(date!=null){
			// sb.append(", ");
			// sb.append(String.format("%1$tm/%1$te %1$tH:%1$tM", date));
			// }
			// sb.append("]");
			// sb.append(" ");
		}

		sb.append("</span>");

		AccountInfo ca = doc.get_caccount();
		if (ca != null) {

			sb.append(" ");
			sb.append(ca.getUserName());
			sb.append("|");
			sb.append(ca.getUserId());
		}

		sb.append("</span>");

		// 显示文档图标
		sb.append("<img src='");
		sb.append(doc.getTypeIconURL());
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />");

		if (doc.isLocked()) {
			sb.append("<img src='");
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_LOCK_16,
					BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
			sb.append("' style='position:absolute; left:14; bottom:4; display:block;' width='16' height='16' />");
		}

		// 显示文件名称
		String desc = doc.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(desc);
		String docNum = doc.getDocumentNumber();
		if (!Utils.isNullOrEmpty(docNum)) {
			sb.append("|");
			sb.append(docNum);
		}

		// 显示版本
		String rev = doc.getRevId();
		sb.append(" <b>");
		sb.append("Rev:");
		sb.append(rev);
		sb.append("</b>");

		// 下载链接
		sb.append("<span style='padding-left:4px'>");
		sb.append("<a href=\"" + doc.get_id().toString() + "@" + "downloadall"
				+ "\" target=\"_rwt\">");
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_DOWNLOAD_15X10,
				BusinessResource.PLUGIN_ID, BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;padding-top:4px;margin:0px' width='15' height='10' />");
		sb.append("</a>");
		sb.append("</span>");

		sb.append("<br/>");
		sb.append("<small>");
		// 显示创建时间，创建人
		Date date = doc.get_cdate();
		sb.append("<span style='float:right;padding-right:14px'>");
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
