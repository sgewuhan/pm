package com.sg.business.model;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;

public class BulletinBoard extends PrimaryObject {

	/**
	 * 发布/回复人
	 */
	public static final String F_PUBLISHER = "publisher";
	
	/**
	 * 发布/回复日期
	 */
	public static final String F_PUBLISH_DATE = "publish_date";
	
	/**
	 * 所属组织ID
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * 标题字段
	 */
	public static final String F_DESC = "desc";

	/**
	 * 内容
	 */
	public static final String F_CONTENT = "content";

	/**
	 * 上级的公告id
	 */
	public static final String F_PARENT_BULLETIN = "parent_id";

	/**
	 * 附件,文件列表型字段
	 */
	public static final String F_ATTACHMENT = "attachment";
	
	/**
	 * 返回标题的显示内容
	 * 
	 * @return String
	 */
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();


		// 添加主题
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);
		sb.append("<b>" + label + "</b>");

		return sb.toString();
	}
}
