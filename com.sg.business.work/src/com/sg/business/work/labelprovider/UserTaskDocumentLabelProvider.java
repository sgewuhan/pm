package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.commons.util.file.OSServerFile;
import com.mobnut.db.file.IServerFile;
import com.mobnut.db.model.AccountInfo;
import com.sg.business.model.Document;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;

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

		return ""; //$NON-NLS-1$

	}

	private String getRemoteFileText(IServerFile serverFile) {
		String fileName = serverFile.getFileName();
		Date uploadDate = serverFile.getUploadDate();
		long length = serverFile.getLength();
		// String md5 = serverFile.getMD5();
		// DBObject meta = gsFile.getMetaData();

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>"); //$NON-NLS-1$

		// 显示文档图标
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_FILE_24,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />"); //$NON-NLS-1$

		// 显示文件名称

		// String url = FileUtil.getDownloadUrl(remoteFile.getDbName(),
		// remoteFile.getNamespace(), remoteFile.getObjectId(), fileName);
		// url = url.replaceAll("\\&", "&amp;");

		if (serverFile instanceof OSServerFile) {
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_OUTREP_16,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='position:absolute; left:14; bottom:4; display:block;' width='16' height='16' />"); //$NON-NLS-1$
		}

		if (serverFile instanceof OSServerFile) {
			sb.append("<a href='"); //$NON-NLS-1$
			String downloadURL = ((OSServerFile) serverFile).getDownloadURL();
			sb.append(downloadURL);
			sb.append("'>"); //$NON-NLS-1$
			sb.append(fileName);
			sb.append("</a>"); //$NON-NLS-1$
		} else {
			sb.append("<a href='download@"); //$NON-NLS-1$
			String downloadURL = serverFile.getInternalDownloadURL();
			sb.append(downloadURL ); 
			sb.append("' target=\"_rwt\">"); //$NON-NLS-1$
			sb.append(fileName);
			sb.append("</a>"); //$NON-NLS-1$

		}

		sb.append("<br/>"); //$NON-NLS-1$
		// 显示大小
		sb.append("<small>"); //$NON-NLS-1$
		sb.append(Messages.get().UserTaskDocumentLabelProvider_0);
		sb.append(getLength(length));
		sb.append(Messages.get().UserTaskDocumentLabelProvider_1);
		sb.append(String.format(Utils.FORMATE_DATE_FULL, uploadDate));
		// if (Portal.getDefault().isDevelopMode()) {
		//			sb.append(" MD5:"); //$NON-NLS-1$
		// sb.append(md5);
		// }
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$
		String text = sb.toString();
		return text;
	}

	private String getLength(long length) {
		if (length >= 1000 * 1000 * 1000) {// TB
			double r = length / (1000 * 1000 * 1000d);
			return String.format("%.2f", r) + "TB"; //$NON-NLS-1$ //$NON-NLS-2$
		} else if (length >= 1000 * 1000) {
			double r = length / (1000 * 1000d);
			return String.format("%.2f", r) + "MB"; //$NON-NLS-1$ //$NON-NLS-2$
		} else if (length >= 1000) {
			double r = length / (1000d);
			return String.format("%.2f", r) + "KB"; //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			return length + "Byte"; //$NON-NLS-1$
		}

	}

	private String getDocumentText(Document doc) {
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

		// 下载链接
//		sb.append("<span style='padding-left:4px'>"); //$NON-NLS-1$
//		sb.append("<a href=\"" + doc.get_id().toString() + "@" + "downloadall" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//				+ "\" target=\"_rwt\">"); //$NON-NLS-1$
//		sb.append("<img src='"); //$NON-NLS-1$
//		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_DOWNLOAD_15X10,
//				BusinessResource.PLUGIN_ID));
//		sb.append("' style='border-style:none;padding-top:4px;margin:0px' width='15' height='10' />"); //$NON-NLS-1$
//		sb.append("</a>"); //$NON-NLS-1$
//		sb.append("</span>"); //$NON-NLS-1$

		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		// 显示创建时间，创建人
		// Date date = doc.get_cdate();
		// sb.append(String.format(Utils.FORMATE_DATE_FULL, date));

		AccountInfo ca = doc.get_caccount();
		if (ca != null) {

			sb.append(" "); //$NON-NLS-1$
			sb.append(ca.getUserName());
			//			sb.append("|"); //$NON-NLS-1$
			// sb.append(ca.getUserId());
		}

		// 显示状态
		String status = doc.getLifecycleName();
		sb.append(" <span style='color:rgb(0,128,0)'>"); //$NON-NLS-1$
		sb.append(status);

		if (doc.isLocked()) {
			sb.append(Messages.get().UserTaskDocumentLabelProvider_2);
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
