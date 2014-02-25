package com.sg.business.model.commonlabel;

import java.util.Date;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.ModelService;
import com.sg.business.model.BulletinBoard;
import com.sg.business.model.Organization;
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
		String publisher = UserToolkit
				.getUserById(bulletinBoard.getPublisher()).getUsername();

		// 设置标题
		String title = bulletinBoard.getLabel();
		title = Utils.getPlainText(title);
		title = Utils.getLimitLengthString(title, 18);

		// 设置内容
		String content = bulletinBoard.getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content, 28);

		Date date = bulletinBoard.getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH,
				date);

		// 设置发布部门
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, bulletinBoard.getOrganizationId()))
				.getDesc();

		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer; border-bottom:1px dotted #cdcdcd;height=100%'>");

		// 显示标题
		sb.append("<div style='"
				+ "font-family:微软雅黑;"
				+ "font-size:10pt;"
				+ "color:#4d4d4d;"
				+ "margin:0 2;"
				+ "display:-moz-inline-box; display:inline-block; "
				+ "overflow:hidden;" 
				+ "white-space:nowrap;"
				+ "text-overflow:ellipsis;" + "'>");
		sb.append(title);
		sb.append("</div>");

		// 显示内容
		sb.append("<div style='"
				+ "font-family:微软雅黑;"
				+ "font-size:8pt;"
				+ "color:#909090;"
				+ "margin:0 2;"
				+ "overflow:hidden;"
				+ "white-space:nowrap;"
				+ "display:-moz-inline-box; "
				+ "display:inline-block; "
				+ "'>");
		sb.append(content);
		sb.append("</div>");

		 sb.append("<div >");
		 sb.append("<br/>");
		 sb.append("<small style='"
					+ "margin:0 2;"
		 		+ "'>");
		 sb.append(org);
		 sb.append("  ");
		 sb.append(publisher);
		 sb.append("  ");
		 sb.append(publishDate);
		
		 sb.append("</small></div>");
		 
		 //回复按钮
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_REPLY_16,
				BusinessResource.PLUGIN_ID));
		sb.append("' style='position:absolute; right:4; bottom:2; display:block;' width='16' height='16' />"); //$NON-NLS-1$

		
		sb.append("</div>"); //$NON-NLS-1$
		return sb.toString();
	}

}
