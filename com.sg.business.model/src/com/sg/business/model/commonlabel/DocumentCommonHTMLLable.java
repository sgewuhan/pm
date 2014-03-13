package com.sg.business.model.commonlabel;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.model.AccountInfo;
import com.mongodb.DBObject;
import com.sg.business.model.Document;
import com.sg.business.model.Project;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class DocumentCommonHTMLLable extends CommonHTMLLabel {

	private Document doc;

	public DocumentCommonHTMLLable(Document document) {
		this.doc = document;
	}

	@Override
	public String getHTML() {
		if ("inlist".equals(key)) {
			return getHTMLInHomeList();
		} else if("search".equals(key)){
			return getHTMLInSearch();
		} else {
			return getHTMLCommmon();
		}
	}

	private String getHTMLInSearch() {
		String[] teams = (String[]) getViewer().getControl().getData("searchteams");
		
		// ��ʾ�ļ�����
		String desc = doc.getDesc();
		desc = Utils.getPlainText(desc);
		String content = (String) doc.getValue(Document.F_CONTENT);
		if(content!=null){
			content = Utils.getPlainText(content);
			content = content.replaceAll("  ", " ");
			content = content.replaceAll("\\\n", " ");
			content = Utils.getLimitLengthString(content, 180);
			for (int i = 0; i < teams.length; i++) {
				content =content.replaceAll(teams[i], "<span style='color:#ff0000'>"+teams[i]+"</span>");
			}

		}else{
			content = "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer;'>");
		
		sb.append("<div style='"
				+ "font-family:΢���ź�;"
//				+ "font-weight:bold;"
				+ "font-size:12pt;"
				+ "margin:0 2;"
				+ "color:#4d4d4d;"
				+ "white-space:nowrap;"
				+ "'><span style='font-weight:bold;'>"); //$NON-NLS-1$

		sb.append(desc);
		sb.append("</span>");

		// ��ʾ�汾
		String rev = doc.getRevId();
		sb.append(" <small><b>"); //$NON-NLS-1$
		sb.append(rev);
		sb.append("</b></small>"); //$NON-NLS-1$

		// ��ʾ״̬
		String status = doc.getLifecycleName();
		sb.append(" <small style='color:#1e0fbe;'>"); //$NON-NLS-1$
		sb.append(status);

		if (doc.isLocked()) {
			sb.append(Messages.get(getLocale()).UserTaskDocumentLabelProvider_2);
		}
		
		sb.append("</small><small> "); //$NON-NLS-1$
		// ��ʾ����ʱ�䣬������
		Date date = doc.get_cdate();
		sb.append(String.format(Utils.FORMATE_DATE_FULL, date));
		
		AccountInfo ca = doc.get_caccount();
		if (ca != null) {
			sb.append(" "); //$NON-NLS-1$
			sb.append(ca.getUserName());
		}
		sb.append("</small></div>");//$NON-NLS-1$

		sb.append("<div style='"
				+ "color:#006621;"
				+ "font-size:8pt;"
				+ "margin:0 2;"
				+ "'>"); //$NON-NLS-1$
		// �������Ŀ�ĵ�����ʾ��Ŀ����
		sb.append("��Ŀ:");
		Project project = doc.getProject();
		if (project != null) {
			desc = project.getDesc();
			desc = Utils.getPlainText(desc);
			sb.append(desc);
		}
		sb.append("</div>");
		
		sb.append("<div>");
		
		sb.append("<div style='"
				+ "color:#909090;"
				+ "font-size:8pt;"
				+"text-overflow:ellipsis;"
				+ "margin:2 2;"
				+ "height:20px;"
				+ "'>"); //$NON-NLS-1$
		sb.append(content);
		sb.append("</div>");

//		sb.append(HtmlUtil.createBottomLine(0)); 
		sb.append("</div>");
		return sb.toString();
	}

	/**
	 * ���һ���HTMLLabel
	 * 
	 * @return
	 */
	private String getHTMLCommmon() {
		Object configurator = getData();
		boolean control = (configurator instanceof ColumnConfigurator)
				&& "workdelivery".equals(((ColumnConfigurator) configurator)
						.getName());

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='font-family:΢���ź�;font-size:9pt'>"); //$NON-NLS-1$

		// ��ʾ�ĵ�ͼ��
		sb.append("<img src='"); //$NON-NLS-1$
		sb.append(doc.getTypeIconURL());
		sb.append("' style='border-style:none;float:left;padding:0px;margin:0px' width='24' height='24' />"); //$NON-NLS-1$

		if (doc.isLocked()) {
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_LOCK_16,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='position:absolute; left:14; bottom:4; display:block;' width='16' height='16' />"); //$NON-NLS-1$
		}

		// ��ʾ�ļ�����
		String desc = doc.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(desc);
		String docNum = doc.getDocumentNumber();
		if (!Utils.isNullOrEmpty(docNum)) {
			sb.append("|"); //$NON-NLS-1$
			sb.append(docNum);
		}

		// ��ʾ�汾
		String rev = doc.getRevId();
		sb.append(" <b>"); //$NON-NLS-1$
		sb.append("Rev:"); //$NON-NLS-1$
		sb.append(rev);
		sb.append("</b>"); //$NON-NLS-1$

		sb.append("<br/>"); //$NON-NLS-1$
		sb.append("<small>"); //$NON-NLS-1$
		// ��ʾ����ʱ�䣬������
		// Date date = doc.get_cdate();
		// sb.append(String.format(Utils.FORMATE_DATE_FULL, date));

		AccountInfo ca = doc.get_caccount();
		if (ca != null) {

			sb.append(" "); //$NON-NLS-1$
			sb.append(ca.getUserName());
		}

		// ��ʾ״̬
		String status = doc.getLifecycleName();
		sb.append(" <span style='color:rgb(0,128,0)'>"); //$NON-NLS-1$
		sb.append(status);

		if (doc.isLocked()) {
			sb.append(Messages.get(getLocale()).UserTaskDocumentLabelProvider_2);
		}

		sb.append("</span>"); //$NON-NLS-1$

		// String summary = doc.getSummary();
		// if (summary != null) {
		// sb.append(Messages.get().UserTaskDocumentLabelProvider_3);
		// String plainText = Utils.getPlainText(summary);
		// plainText = Utils.getLimitLengthString(plainText, 40);
		// sb.append(plainText);
		// }
		sb.append("</small>"); //$NON-NLS-1$

		sb.append("</span>"); //$NON-NLS-1$

		// �ϴ�
		if (!doc.isLocked() && control) {
			sb.append("<a href=\"upload@" + doc.get_id().toString() //$NON-NLS-1$ 
					+ "\" target=\"_rwt\">"); //$NON-NLS-1$
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_ADD_G_24,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='border-style:none;position:absolute; right:8; bottom:8; display:block;' width='24' height='24' />"); //$NON-NLS-1$
			sb.append("</a>");
		}

		// ����
		if (control) {
			sb.append("<a href=\"downloadall@" + doc.get_id().toString() //$NON-NLS-1$ 
					+ "\" target=\"_rwt\">"); //$NON-NLS-1$
			sb.append("<img src='"); //$NON-NLS-1$
			sb.append(FileUtil.getImageURL(BusinessResource.IMAGE_DOWN_G_24,
					BusinessResource.PLUGIN_ID));
			sb.append("' style='border-style:none;position:absolute; right:40; bottom:8; display:block;' width='24' height='24' />"); //$NON-NLS-1$
			sb.append("</a>");
		}

		return sb.toString();
	}

	/**
	 * ��ҳ���б���ʾ
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getHTMLInHomeList() {
		StructuredViewer viewer = getViewer();
		// ��ʾ�ļ�����
		String desc = doc.getDesc();
		desc = Utils.getPlainText(desc);
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer;'>");
		
		sb.append("<div style='"
				+ "font-family:΢���ź�;"
				+ "font-size:10pt;"
				+ "margin:0 2;"
				+ "color:#4d4d4d;"
				+ "white-space:nowrap;"
				+ "'>"); //$NON-NLS-1$

		sb.append(desc);

		// ��ʾ�汾
		String rev = doc.getRevId();
		sb.append(" <small><b>"); //$NON-NLS-1$
		sb.append(rev);
		sb.append("</b></small>"); //$NON-NLS-1$

		// ��ʾ״̬
		String status = doc.getLifecycleName();
		sb.append(" <small style='color:rgb(0,128,0);'>"); //$NON-NLS-1$
		sb.append(status);

		if (doc.isLocked()) {
			sb.append(Messages.get(getLocale()).UserTaskDocumentLabelProvider_2);
		}
		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</div>");//$NON-NLS-1$

		sb.append("<div style='"
				+ "color:#909090;"
				+ "font-size:8pt;"
				+ "margin:0 2;"
				+ "'>"); //$NON-NLS-1$
		// �������Ŀ�ĵ�����ʾ��Ŀ����
		sb.append("��Ŀ:");
		Project project = doc.getProject();
		if (project != null) {
			desc = project.getDesc();
			desc = Utils.getPlainText(desc);
			sb.append(desc);
		}
		sb.append("</div>");
		
		sb.append("<br/>");
		sb.append("<small style='"
				+ "position:absolute;"
				+ "right:4; bottom:2; "
				+ "display:block;"
				+ "'>");

		// ��ʾ����ʱ�䣬������
//		Date date = doc.get_cdate();
//		sb.append(String.format(Utils.FORMATE_DATE_FULL, date));

		AccountInfo ca = doc.get_caccount();
		if (ca != null) {
			sb.append(" "); //$NON-NLS-1$
			sb.append(ca.getUserName());
		}

		sb.append("</small>"); //$NON-NLS-1$

		List<DBObject> input = (List<DBObject>) viewer.getInput();
		if (input != null) {
			// �ж������ǰ�ĵ������һ��������ʾ�ָ���
			int i;
			for (i = 0; i < input.size(); i++) {
				DBObject dbObject = input.get(i);
				Object id = dbObject.get("id");
				if (doc.get_id().equals(id)) {
					break;
				}
			}
			if (i != input.size() - 1||input.size()<4) {
				sb.append(HtmlUtil.createBottomLine(0)); 
			}
		}
		sb.append("</div>");
		return sb.toString();
	}

}
