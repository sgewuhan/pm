package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.PrimaryObject;

public class BulletinBoard extends PrimaryObject {

	/**
	 * ����/�ظ���
	 */
	public static final String F_PUBLISHER = "publisher";

	/**
	 * ����/�ظ�����
	 */
	public static final String F_PUBLISH_DATE = "publish_date";

	/**
	 * ������֯ID
	 */
	public static final String F_ORGANIZATION_ID = "organization_id";

	/**
	 * �����ֶ�
	 */
	public static final String F_DESC = "desc";

	/**
	 * ����
	 */
	public static final String F_CONTENT = "content";

	/**
	 * �ϼ��Ĺ���id
	 */
	public static final String F_PARENT_BULLETIN = "parent_id";

	/**
	 * ����,�ļ��б����ֶ�
	 */
	public static final String F_ATTACHMENT = "attachment";

	public static final String EDITOR_REPLY = "bulletinboard.editor.reply";

	public static final String EDITOR_CREATE = "bulletinboard.editor.create";

	public String getPublisher() {
		return (String) getValue(F_PUBLISHER);
	}

	public String getPublishDate() {
		return (String) getValue(F_PUBLISH_DATE);
	}

	public String getOrganizationId() {
		return (String) getValue(F_ORGANIZATION_ID);
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
	 * ���ر������ʾ����
	 * 
	 * @return String
	 */
	public String getHTMLLabel() {
		StringBuffer sb = new StringBuffer();

		// �������
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date date = (Date) getValue(F_PUBLISH_DATE);
		String publishDate = sdf.format(date);
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(publishDate);
		sb.append("</span>");
		
		// ��ӱ���
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);
		sb.append("<b>" + label + "</b>");

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
		return userId.equals(bulletinboardUserid);
	}

	public boolean isReply() {
		if (getParentBulletin() == null) {
			return false;
		} else {
			return true;
		}
	}

}
