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
 * 公告板
 * 
 * @author yangjun
 * 
 */
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
	 * 所属项目ID
	 */
	public static final String F_PROJECT_ID = "project_id";

	/**
	 * 公告内容
	 */
	public static final String F_CONTENT = "content";

	/**
	 * 上级公告id
	 */
	public static final String F_PARENT_BULLETIN = "parent_id";

	/**
	 * 所有的上级
	 */
	public static final String F_SUPER_BULLETIN = "supers";

	/**
	 * 附件,文件列表型字段
	 */
	public static final String F_ATTACHMENT = "attachment";

	/**
	 * 回复编辑器
	 */
	public static final String EDITOR_REPLY = "bulletinboard.editor.reply";

	/**
	 * 公告编辑器
	 */
	public static final String EDITOR_CREATE = "bulletinboard.editor.create";

	/**
	 * @return : {@link String},发起/回复人
	 */
	public String getPublisher() {
		return (String) getValue(F_PUBLISHER);
	}

	/**
	 * @return : {@link Date},发起/回复日期
	 */
	public Date getPublishDate() {
		return (Date) getValue(F_PUBLISH_DATE);
	}

	/**
	 * @return : {@link ObjectId},所属组织ID
	 */
	public ObjectId getOrganizationId() {
		return (ObjectId) getValue(F_ORGANIZATION_ID);
	}

	/**
	 * @return : {@link ObjectId},所属项目ID
	 */
	public ObjectId getProjectId() {
		return (ObjectId) getValue(F_PROJECT_ID);
	}

	/**
	 * @return : {@link String},公告内容
	 */
	public String getContent() {
		return (String) getValue(F_CONTENT);
	}

	/**
	 * @return : {@link ObjectId},上级公告ID
	 */
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
		// 设置发布人
		String publisher = UserToolkit.getUserById(getPublisher())
				.getUsername();

		// 设置标题
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);

		// 设置内容
		String content = getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content, 40);

		Date date = getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH, date);

		// 设置发布部门
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, getOrganizationId())).getDesc();

		// 显示标题和内容
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:9pt'><b>");

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
	 * 返回发布人的显示内容
	 * 
	 * @return String
	 */
	public String getPublisherLabel() {
		StringBuffer sb = new StringBuffer();

		// 设置发布人
		String publisher = UserToolkit.getUserById(getPublisher())
				.getUsername();
		// 设置日期
		Date date = getPublishDate();
		String publishDate = String.format(Utils.FORMATE_DATE_COMPACT_SASH, date);

		sb.append("<span style='padding-left:4px'>");
		sb.append(publisher);
		sb.append("</span>");

		sb.append("<span style='float:right;padding-right:4px'>");
		sb.append(publishDate);
		sb.append("</span>");

		sb.append("<br/>");

		// 设置发布部门
		String org = ((Organization) ModelService.createModelObject(
				Organization.class, getOrganizationId())).getDesc();
		sb.append("<span style='padding-left:4px'>");
		sb.append(org);
		sb.append("</span>");

		return sb.toString();
	}

	/**
	 * 根据发布人和当前登陆用户判断能否编辑，一致时才能编辑。
	 */
	@Override
	public boolean canEdit(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canEdit(context);
	}

	/**
	 * 根据发布人和当前登陆用户判断能否读取，一致时才能读取。
	 */
	@Override
	public boolean canRead(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canRead(context);
	}

	/**
	 * 根据发布人和当前登陆用户判断能否删除，一致时才能删除。
	 */
	@Override
	public boolean canDelete(IContext context) {
		if (isOtherUser(context)) {
			return false;
		}
		return super.canDelete(context);
	}

	/**
	 * 判断当前用户是否为发布人
	 * 
	 * @param context
	 *            : 当前情景
	 * @return : {@link boolean}
	 */
	private boolean isOtherUser(IContext context) {
		// 获取当前登录用户
		String userId = context.getAccountInfo().getUserId();
		// 获取发布人
		String bulletinboardUserid = getPublisher();

		return !userId.equals(bulletinboardUserid);
	}

	public boolean currentUserSessioncanEdit() {
		CurrentAccountContext context = new CurrentAccountContext();
		return !isOtherUser(context);
	}

	@Override
	public String getTypeName() {
		return "公告";
	}

	@Override
	public Image getImage() {
		return BusinessResource.getImage(BusinessResource.IMAGE_BULLETING_16);
	}

	@SuppressWarnings("unchecked")
	public BulletinBoard makeReply(BulletinBoard reply) {
		// 设置新公告板的上级公告ID
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
