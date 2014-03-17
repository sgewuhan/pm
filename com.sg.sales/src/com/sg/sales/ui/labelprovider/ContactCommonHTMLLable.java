package com.sg.sales.ui.labelprovider;

import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.sg.sales.model.Company;
import com.sg.sales.model.Contact;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class ContactCommonHTMLLable extends CommonHTMLLabel {

	private Contact contact;

	public ContactCommonHTMLLable(Contact contact) {
		this.contact = contact;
	}

	@Override
	public String getHTML() {
		if ("inlist".equals(key)) {
			return getHTMLInHomeList();
		} else {
			return getHTMLCommmon();
		}

	}

	/**
	 * 获得一般的HTMLLabel
	 * 
	 * @return
	 */
	private String getHTMLCommmon() {
		return contact.getHTMLLabel();
	}

	/**
	 * 首页的列表显示
	 * 
	 * @return
	 */
	private String getHTMLInHomeList() {
		String headURL = getFirstHeadPicURL();
		if (headURL == null) {
			return getHTMLWithoutPic();
		} else {
			return getHTMLWithPic(headURL);
		}
	}

	private String getHTMLWithPic(String headURL) {
		String name = contact.getName();
		
		String email = contact.getEmail();
		
		String tel = contact.getOfficeTelNumber();
		
		Company company = contact.getCompany();
		String companyName = "";
		if(company!=null){
			companyName = company.getDesc();
		}
		
		String position = contact.getPosition();
		String dept = contact.getDepartment();
		
		StringBuffer sb = new StringBuffer();
		String imageUrl = "<img src='" + headURL //$NON-NLS-1$
				+ "' style='position:absolute; left:0px; top:0px;' width='50' height='50' "
				+ "/>"; //$NON-NLS-1$
		sb.append(imageUrl);

		sb.append("<div style='margin:0 0 0 52;" + "cursor:pointer; "
				+ "width=100%;" + "'>");

		// 显示标题
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:10pt;"
				+ "color:#4d4d4d;" + "margin:0 2;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "overflow:hidden;" 
//				+ "width=" + (width - 70) + ";"
				+ "white-space:nowrap;" + "text-overflow:ellipsis;" + "'" + ">");
		sb.append(name+" "+position);

		sb.append("</div>");

		// 显示内容
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:8pt;"
				+ "color:#909090;" + "margin:0 2;" + "overflow:hidden;"
				+ "white-space:nowrap;" 
//				+ "width=" + (width - 70) + ";"
				+ "text-overflow:ellipsis;" + "'>");
		sb.append(companyName+" "+dept+"<br/>"+tel+" ");
		sb.append(" <a href='mailto:" + email + "' target='_black'>"
				+ email + "</a>");
//		sb.append(email);

		sb.append("</div>");
//		sb.append(HtmlUtil.createBottomLine(51)); //$NON-NLS-1$

		return sb.toString();
	}

	private String getHTMLWithoutPic() {
		String name = contact.getName();
		
		String email = contact.getEmail();
		email = email==null?"":email;
		
		String tel = contact.getOfficeTelNumber();
		tel = tel==null?"":tel;

		Company company = contact.getCompany();
		String companyName = "";
		if(company!=null){
			companyName = company.getDesc();
		}
		
		String position = contact.getPosition();
		position = position==null?"":position;
		String dept = contact.getDepartment();
		dept = dept==null?"":dept;
		

		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer;'>");

		// 显示标题
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:10pt;"
				+ "color:#4d4d4d;" + "margin:0 2;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "overflow:hidden;" 
//				+ "width=" + (width - 6) + ";"
				+ "white-space:nowrap;" + "text-overflow:ellipsis;" + "'>");
		sb.append(name+" "+position);
		sb.append("</div>");

		// 显示内容
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:8pt;"
				+ "color:#909090;" + "margin:0 2;" + "overflow:hidden;"
				+ "white-space:nowrap;" 
//				+ "width=" + (width - 6) + ";"
				+ "text-overflow:ellipsis;" + "'>");
		sb.append(companyName+" "+dept+"<br/>"+tel+" ");
		sb.append(" <a href='mailto:" + email + "' target='_black'>"
				+ email + "</a>");
		sb.append("</div>");

//		sb.append(HtmlUtil.createBottomLine(0)); //$NON-NLS-1$
		return sb.toString();
	}

	private String getFirstHeadPicURL() {
		List<RemoteFile> headpics = contact.getPhotos();
		if (headpics != null && headpics.size() > 0) {
			try {
				return FileUtil.getImageURL(headpics.get(0).getNamespace(),
						new ObjectId(headpics.get(0).getObjectId()), headpics
								.get(0).getDbName());
			} catch (Exception e) {
			}
		}

		return null;
	}

}
