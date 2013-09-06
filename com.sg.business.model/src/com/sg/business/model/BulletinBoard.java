package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

/**
 * �����
 * @author yangjun
 *
 */
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
	 * ����
	 */
	public static final String F_DESC = "desc";

	/**
	 * ��������
	 */
	public static final String F_CONTENT = "content";

	/**
	 * �ϼ�����id
	 */
	public static final String F_PARENT_BULLETIN = "parent_id";

	/**
	 * ����,�ļ��б����ֶ�
	 */
	public static final String F_ATTACHMENT = "attachment";

	/**
	 * �ظ��༭��
	 */
	public static final String EDITOR_REPLY = "bulletinboard.editor.reply";

	/**
	 * ����༭��
	 */
	public static final String EDITOR_CREATE = "bulletinboard.editor.create";

	/**
	 * @return : {@link String},����/�ظ���
	 */
	public String getPublisher() {
		return (String) getValue(F_PUBLISHER);
	}

	/**
	 * @return : {@link Date},����/�ظ�����
	 */
	public Date getPublishDate() {
		return (Date) getValue(F_PUBLISH_DATE);
	}

	/**
	 * @return : {@link ObjectId},������֯ID
	 */
	public ObjectId getOrganizationId() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * @return : {@link String},��������
	 */
	public String getContent() {
		return (String) getValue(F_CONTENT);
	}

	/**
	 * @return : {@link ObjectId},�ϼ�����ID
	 */
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

		// ���ñ���
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);
		
		//��������
		String content = getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content,40);
		
		//�ж��Ƿ�Ϊ�ظ����ݣ��ظ�����ʱ��ֻ��ʾ���ݣ�����ʱ����ʾ���������
		if (isReply()) {
			sb.append(content);
		} else {
			sb.append("<b>" + label + "</b>");
			sb.append("<br/>");
			sb.append(content);
		}
		return sb.toString();
	}
	
	/**
	 * ���ط����˵���ʾ����
	 * 
	 * @return String
	 */
	public String getPublisherLabel() {
		StringBuffer sb = new StringBuffer();

		//���÷�����
		String punlisher =User.getUserById(getPublisher()).getUsername();
		//��������
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date date = getPublishDate();
		String publishDate = sdf.format(date);
		//���÷�������
		String org = ((Organization)ModelService.createModelObject(Organization.class,
				getOrganizationId())).getDesc();

		sb.append("<span style='padding-left:4px'>");
		sb.append("" + punlisher);
		sb.append("</span>");
		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append("" + publishDate);
		sb.append("</span>");
		sb.append("<br/>");
		sb.append(""+org);

		return sb.toString();
	}
	
	/**
	 * ���ݷ����˺͵�ǰ��½�û��ж��ܷ�༭��һ��ʱ���ܱ༭��
	 */
	@Override
	public boolean canEdit(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canEdit(context);
	}

	/**
	 * ���ݷ����˺͵�ǰ��½�û��ж��ܷ��ȡ��һ��ʱ���ܶ�ȡ��
	 */
	@Override
	public boolean canRead(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canRead(context);
	}

	/**
	 * ���ݷ����˺͵�ǰ��½�û��ж��ܷ�ɾ����һ��ʱ����ɾ����
	 */
	@Override
	public boolean canDelete(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canDelete(context);
	}

	/**
	 * �жϵ�ǰ�û��Ƿ�Ϊ������
	 * @param context : ��ǰ�龰
	 * @return : {@link boolean}
	 */
	private boolean isOtherUser(IContext context) {
		//��ȡ��ǰ��¼�û�
		String userId = context.getAccountInfo().getUserId();
		//��ȡ������
		String bulletinboardUserid = getPublisher();
		if (userId == bulletinboardUserid) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �ж��Ƿ�Ϊ�ظ���Ϣ
	 * @return : {@link boolean}
	 */
	public boolean isReply() {
		//�����ϼ�����ID�ж��Ƿ�Ϊ�ظ���Ϣ
		if (getParentBulletin() == null) {
			return false;
		} else {
			return true;
		}
	}

}
