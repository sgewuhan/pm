package com.sg.business.work.labelprovider;

import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.commons.util.file.OSServerFile;
import com.mobnut.db.file.IServerFile;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.HTMLAdvanceLabelProvider;

public class UserTaskDocumentLabelProvider extends HTMLAdvanceLabelProvider {


	@Override
	public String getText(Object element) {
		if (element instanceof IServerFile) {
			IServerFile serverFile = (IServerFile) element;
			return getRemoteFileText(serverFile);
		}

		return super.getText(element);

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
			sb.append("  ");
			sb.append("<a href='view@");
			sb.append("' target=\"_rwt\">"); 
			sb.append("预览");
			sb.append("</a>"); 
		} else {
			sb.append("<a href='download@"); //$NON-NLS-1$
			String downloadURL = serverFile.getInternalDownloadURL();
			sb.append(downloadURL ); 
			sb.append("' target=\"_rwt\">"); //$NON-NLS-1$
			sb.append(fileName);
			sb.append("</a>"); //$NON-NLS-1$
			sb.append("  ");
			sb.append("<a href='view@");
			sb.append("' target=\"_rwt\">");  
			sb.append("预览");
			sb.append("</a>"); 
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

}
