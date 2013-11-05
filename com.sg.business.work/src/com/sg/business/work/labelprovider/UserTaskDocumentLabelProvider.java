package com.sg.business.work.labelprovider;

import java.util.Date;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.AccountInfo;
import com.mongodb.gridfs.GridFSFile;
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
		} else if(element instanceof RemoteFile){
			RemoteFile remoteFile = (RemoteFile) element;
			return getRemoteFileText(remoteFile);
		}
		
		return "";

	}

	private String getRemoteFileText(RemoteFile remoteFile) {
		String fileName = remoteFile.getFileName();
		GridFSFile gsFile = remoteFile.getGridFSFile();
		Date uploadDate = gsFile.getUploadDate();
		long length = gsFile.getLength();
//		String md5 = gsFile.getMD5();
//		DBObject meta = gsFile.getMetaData();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		sb.append("<span style='float:right;padding-right:14px'>");
		sb.append(String.format(Utils.FORMATE_DATE_FULL, uploadDate));
		sb.append("</span>");

		// 显示文档图标
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_FILE_24, BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />");

		// 显示文件名称
		sb.append(fileName);

		

		
		sb.append("<br/>");
		// 显示大小
		sb.append(getLength(length));
		sb.append("<small>");

		sb.append("</small>");

		sb.append("</span>");
		return sb.toString();
		
	}

	private String getLength(long length) {
		if(length>=1000*1000*1000){//TB
			double r = length/(1000*1000*1000d);
			return String.format("%.2f",r)+"TB"; 
		}else if(length>=1000*1000){
			double r = length/(1000*1000d);
			return String.format("%.2f",r)+"MB"; 
		}else if(length>=1000){
			double r = length/(1000d);
			return String.format("%.2f",r)+"KB"; 
		}else{
			return length +"Byte";
		}
			
	}
	
	public static void main(String[] args) {
		long length = 928388338;
		double r = length/(1000*1000*1000d);
		System.out.println(String.format("%.2f",r));
	}

	private String getDocumentText(Document doc) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'>");

		sb.append("<span style='float:right;padding-right:14px'>");
		AccountInfo ca = doc.get_caccount();
		sb.append(ca.getUserName());
		sb.append("|");
		sb.append(ca.getUserId());
		sb.append("</span>");
		
		//下载链接
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append("<a href=\"" + doc.get_id().toString() + "@"
				+ "downloadall" + "\" target=\"_rwt\">");
		sb.append("<img src='");
		sb.append(FileUtil.getImageURL(
				BusinessResource.IMAGE_DOWNLOAD_16X12,
				BusinessResource.PLUGIN_ID,
				BusinessResource.IMAGE_FOLDER));
		sb.append("' style='border-style:none;padding-top:4px;margin:0px' width='16' height='12' />");
		sb.append("</a>");
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
		sb.append(" <b>");
		sb.append("Rev:");
		sb.append(rev);
		sb.append("</b>");
		
		// 显示状态
		String status = doc.getLifecycleName();
		sb.append(" <span style='color:rgb(0,128,0)'>");
		sb.append(status);
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
