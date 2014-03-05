package com.sg.business.model.commonlabel;

import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class BulletinBoardCommonHTMLLable extends CommonHTMLLabel {

	private BulletinBoard bulletinBoard;

	public BulletinBoardCommonHTMLLable(BulletinBoard bulletinBoard) {
		this.bulletinBoard = bulletinBoard;
	}

	@Override
	public String getHTML() {

		// 设置发布人
		User user = UserToolkit.getUserById(bulletinBoard.getPublisher());
		String headURL = user.getFirstHeadPicURL();
		if (headURL == null) {
			return getHTMLWithoutPic(user);
		} else {
			return getHTMLWithPic(user, headURL);
		}

	}

	private String getHTMLWithPic(User user, String headURL) {
		int width = getWidthHint();
		String publisher = user.getUsername();

		// 设置标题
		String title = bulletinBoard.getLabel();
		title = Utils.getPlainText(title);
		title = Utils.getLimitLengthString(title, 16);

		// 设置内容
		String content = bulletinBoard.getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content, 18);

		Date date = bulletinBoard.getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
				date);

		// 设置发布部门
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, bulletinBoard.getOrganizationId()))
				.getDesc();

		StringBuffer sb = new StringBuffer();
		String imageUrl = "<img src='" + headURL //$NON-NLS-1$
				+ "' style='position:absolute; left:0px; top:0px;' width='64' height='64' "
				// + " onmouseover=\"this.src='"
				// + FileUtil.getImageURL(BusinessResource.IMAGE_CREATEWORK_24,
				// BusinessResource.PLUGIN_ID)
				// + "'\";"
				// + " onmouseout=\"this.src='"
				// +headURL
				// + "'\";"
				+ "/>"; //$NON-NLS-1$
		sb.append(imageUrl);

		sb.append("<div style='margin:0 0 0 64;" + "cursor:pointer; "
				+ "border-bottom:1px dotted #cdcdcd;" + "height=100%;"
				+ "width=100%;" + "'>");

		// 显示标题
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:10pt;"
				+ "color:#4d4d4d;" + "margin:0 2;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "overflow:hidden;" + "width=" + (width - 70) + ";"
				+ "white-space:nowrap;" + "text-overflow:ellipsis;" + "'" + ">");
		sb.append(title);
		sb.append("</div>");

		// 显示内容
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:8pt;"
				+ "color:#909090;" + "margin:0 2;" + "overflow:hidden;"
				+ "white-space:nowrap;" + "width=" + (width - 70) + ";"
				// + "display:-moz-inline-box; "
				// + "display:inline-block; "
				+ "text-overflow:ellipsis;" + "'>");
		sb.append(content);
		sb.append("</div>");

		sb.append("<div >");
		sb.append("<br/>");
		sb.append("<small style='" + "margin:0 2;" + "'>");
		sb.append(org);
		sb.append("  ");
		sb.append(publisher);
		sb.append("  ");
		sb.append(publishDate);

		sb.append("</small></div>");

		// 回复按钮
		sb.append("<a href=\"replybulletinboard@" + bulletinBoard.get_id().toString() //$NON-NLS-1$ 
				+ "\" target=\"_rwt\">"); //$NON-NLS-1$
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_REPLY_16,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;position:absolute; right:4; bottom:2; display:block;' width='16' height='16' />"); //$NON-NLS-1$
		sb.append("</a>");//$NON-NLS-1$

		sb.append("</div>"); //$NON-NLS-1$
		return sb.toString();
	}

	private String getHTMLWithoutPic(User user) {

		int width = getWidthHint();

		String publisher = user.getUsername();

		// 设置标题
		String title = bulletinBoard.getLabel();
		title = Utils.getPlainText(title);
		title = Utils.getLimitLengthString(title, 16);

		// 设置内容
		String content = bulletinBoard.getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content, 18);

		Date date = bulletinBoard.getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
				date);

		// 设置发布部门
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, bulletinBoard.getOrganizationId()))
				.getDesc();

		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer; border-bottom:1px dotted #cdcdcd;height=100%;width="
				+ width + "'>");

		// 显示标题
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:10pt;"
				+ "color:#4d4d4d;" + "margin:0 2;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "overflow:hidden;" + "width=" + (width - 6) + ";"
				+ "white-space:nowrap;" + "text-overflow:ellipsis;" + "'>");
		sb.append(title);
		sb.append("</div>");

		// 显示内容
		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:8pt;"
				+ "color:#909090;" + "margin:0 2;" + "overflow:hidden;"
				+ "white-space:nowrap;" + "width=" + (width - 6) + ";"
				// + "display:-moz-inline-box; "
				// + "display:inline-block; "
				+ "text-overflow:ellipsis;" + "'>");
		sb.append(content);
		sb.append("</div>");

		sb.append("<div >");
		sb.append("<br/>");
		sb.append("<small style='" + "margin:0 2;" + "'>");
		sb.append(org);
		sb.append("  ");
		sb.append(publisher);
		sb.append("  ");
		sb.append(publishDate);

		sb.append("</small></div>");

		// 回复按钮
		sb.append("<a href=\"replybulletinboard@" + bulletinBoard.get_id().toString() //$NON-NLS-1$ 
				+ "\" target=\"_rwt\">"); //$NON-NLS-1$
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_REPLY_16,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='border-style:none;position:absolute; right:4; bottom:2; display:block;' width='16' height='16' />"); //$NON-NLS-1$
		sb.append("</a>");//$NON-NLS-1$

		sb.append("</div>"); //$NON-NLS-1$
		return sb.toString();
	}

}
