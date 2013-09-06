package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
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

	public static final String EDITOR_REPLY = "bulletinboard.editor.reply";

	public static final String EDITOR_CREATE = "bulletinboard.editor.create";

	public String getPublisher() {
		return (String) getValue(F_PUBLISHER);
	}

	public Date getPublishDate() {
		return (Date) getValue(F_PUBLISH_DATE);
	}

	public ObjectId getOrganizationId() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	public String getDesc() {
		return (String) getValue(F_DESC);
	}

	public String getContent() {
		return (String) getValue(F_CONTENT);
	}

	public ObjectId getParentBulletin() {
		return (ObjectId) getValue(F_PARENT_BULLETIN);
	}

	/**
	 * 返回标题的显示内容
	 * 
	 * @return String
	 */
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();

		if (isReply()) {
			
		} else {
			// 添加标题
			String label = getLabel();
			label = Utils.getPlainText(label);
			label = Utils.getLimitLengthString(label, 20);
			sb.append("<b>" + label + "</b>");
		}
		return sb.toString();
	}

	@Override
	public boolean canEdit(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canEdit(context);
	}

	@Override
	public boolean canRead(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canRead(context);
	}

	@Override
	public boolean canDelete(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canDelete(context);
	}

	private boolean isOtherUser(IContext context) {
		String userId = context.getAccountInfo().getUserId();
		String bulletinboardUserid = getPublisher();
		if (userId == bulletinboardUserid) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isReply() {
		if (getParentBulletin() == null) {
			return false;
		} else {
			return true;
		}
	}

}
