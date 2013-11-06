package com.sg.business.model;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;
import org.eclipse.swt.graphics.Image;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.WriteResult;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.part.CurrentAccountContext;

/**
 * �����
 * 
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
	 * ������ĿID
	 */
	public static final String F_PROJECT_ID = "project_id";

	/**
	 * ��������
	 */
	public static final String F_CONTENT = "content";

	/**
	 * �ϼ�����id
	 */
	public static final String F_PARENT_BULLETIN = "parent_id";

	/**
	 * ���е��ϼ�
	 */
	public static final String F_SUPER_BULLETIN = "supers";

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
	 * @return : {@link ObjectId},������ĿID
	 */
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
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
		// ���÷�����
		String publisher = UserToolkit.getUserById(getPublisher())
				.getUsername();

		// ���ñ���
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);

		// ��������
		String content = getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content, 40);

		Date date = getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH, date);

		// ���÷�������
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, getOrganizationId())).getDesc();

		// ��ʾ���������
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:9pt'><b>");

		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(publisher);
		sb.append("  ");
		sb.append(publishDate);

		sb.append("</span>");

		sb.append(label);
		sb.append("</b></span>");

		sb.append("<br/><small>");

		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(org);
		sb.append("</span>");

		sb.append(content);

		sb.append("</small>");

		return sb.toString();
	}

	/**
	 * ���ط����˵���ʾ����
	 * 
	 * @return String
	 */
	public String getPublisherLabel() {
		StringBuffer sb = new StringBuffer();

		// ���÷�����
		String publisher = UserToolkit.getUserById(getPublisher())
				.getUsername();
		// ��������
		Date date = getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH, date);

		sb.append("<span style='padding-left:4px'>");
		sb.append(publisher);
		sb.append("</span>");

		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(publishDate);
		sb.append("</span>");

		sb.append("<br/>");

		// ���÷�������
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, getOrganizationId())).getDesc();
		sb.append("<span style='padding-left:4px'>");
		sb.append(org);
		sb.append("</span>");

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
	 * 
	 * @param context
	 *            : ��ǰ�龰
	 * @return : {@link boolean}
	 */
	private boolean isOtherUser(IContext context) {
		// ��ȡ��ǰ��¼�û�
		String userId = context.getAccountInfo().getUserId();
		// ��ȡ������
		String bulletinboardUserid = getPublisher();

		return !userId.equals(bulletinboardUserid);
	}

	public boolean currentUserSessioncanEdit() {
		CurrentAccountContext context = new CurrentAccountContext();
		return !isOtherUser(context);
	}

	@Override
	public String getTypeName() {
		return "����";
	}

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_BULLETING_16);
	}

	@SuppressWarnings("unchecked")
	public BulletinBoard makeReply(BulletinBoard reply) {
		// �����¹������ϼ�����ID
		if (reply == null) {
			reply = ModelService.createModelObject(BulletinBoard.class);
		}
		reply.setValue(BulletinBoard.F_PARENT_BULLETIN, get_id());
		reply.setValue(BulletinBoard.F_PROJECT_ID, getProjectId());
		Object supers = getValue(F_SUPER_BULLETIN);
		if (!(supers instanceof ArrayList<?>)) {
			supers = new ArrayList<ObjectId>();
		}
		((ArrayList<ObjectId>) supers).add(get_id());
		reply.setValue(F_SUPER_BULLETIN, supers);
		return reply;
	}

	@Override
	public void doRemove(IContext context) throws Exception {
		if(!canDelete(context)){
			return;
		}
		
		DBCollection col = getCollection();
		WriteResult ws = col.remove(new BasicDBObject().append(F_SUPER_BULLETIN, get_id()));
		checkWriteResult(ws);
		super.doRemove(context);
	}

}
