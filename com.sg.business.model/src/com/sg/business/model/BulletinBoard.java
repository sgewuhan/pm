package com.sg.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;

/**
 * 公告板
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
	 * 标题
	 */
	public static final String F_DESC = "desc";

	/**
	 * 公告内容
	 */
	public static final String F_CONTENT = "content";

	/**
	 * 上级公告id
	 */
	public static final String F_PARENT_BULLETIN = "parent_id";

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

		// 设置标题
		String label = getLabel();
		label = Utils.getPlainText(label);
		label = Utils.getLimitLengthString(label, 20);
		
		//设置内容
		String content = getContent();
		content = Utils.getPlainText(content);
		content = Utils.getLimitLengthString(content,40);
		
		//判断是否为回复内容，回复内容时，只显示内容，公告时，显示标题和内容
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
	 * 返回发布人的显示内容
	 * 
	 * @return String
	 */
	public String getPublisherLabel() {
		StringBuffer sb = new StringBuffer();

		//设置发布人
		String punlisher =User.getUserById(getPublisher()).getUsername();
		//设置日期
		SimpleDateFormat sdf = new SimpleDateFormat(Utils.SDF_DATE_COMPACT_SASH);
		Date date = getPublishDate();
		String publishDate = sdf.format(date);
		//设置发布部门
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
	 * @param context : 当前情景
	 * @return : {@link boolean}
	 */
	private boolean isOtherUser(IContext context) {
		//获取当前登录用户
		String userId = context.getAccountInfo().getUserId();
		//获取发布人
		String bulletinboardUserid = getPublisher();
		if (userId == bulletinboardUserid) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否为回复信息
	 * @return : {@link boolean}
	 */
	public boolean isReply() {
		//根据上级公告ID判断是否为回复信息
		if (getParentBulletin() == null) {
			return false;
		} else {
			return true;
		}
	}

}
