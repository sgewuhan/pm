package com.sg.sales.ui.labelprovider;

import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.AccountInfo;
import com.sg.sales.model.Company;
import com.sg.sales.model.Opportunity;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class OpportunityCommonHTMLLable extends CommonHTMLLabel {

	private Opportunity opportunity;

	public OpportunityCommonHTMLLable(Opportunity opportunity) {
		this.opportunity = opportunity;
	}

	@Override
	public String getHTML() {
		if ("inlist".equals(key)) {
			return getHTMLInHomeList();
		} else if ("singleline".equals(key)) {
			return getHTMLSingleLine();
		} else {
			return getHTMLCommmon();
		}

	}


	/**
	 * ���һ���HTMLLabel
	 * 
	 * @return
	 */
	private String getHTMLCommmon() {
		return opportunity.getHTMLLabel();
	}

	/**
	 * ��ҳ���б���ʾ
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getHTMLInHomeList() {
		StructuredViewer viewer = getViewer();
		String desc = opportunity.getDesc();
		desc = Utils.getPlainText(desc);
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer;'>");

		sb.append("<div style='" + "font-family:΢���ź�;" + "font-size:10pt;"
				+ "margin:0 2;" + "color:#4d4d4d;" + "white-space:nowrap;"
				+ "'>"); //$NON-NLS-1$

		sb.append(desc);

		// ��ʾԤ��
		Double rev = opportunity.getBudget();
		sb.append(" <small><b>"); //$NON-NLS-1$
		sb.append("Ԥ��:");
		if (rev == null) {
			sb.append("?");
		} else {
			sb.append(new DecimalFormat(Utils.NF_NUMBER_P2).format(rev
					.doubleValue()));
			sb.append("��");
		}
		sb.append("</b></small>"); //$NON-NLS-1$

		// ��ʾ��չ
		String status = opportunity.getProgress();
		sb.append(" <small style='color:rgb(0,128,0);'>"); //$NON-NLS-1$
		sb.append(status);

		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</div>");//$NON-NLS-1$

		sb.append("<div style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "'>"); //$NON-NLS-1$
		sb.append("�ͻ�:");
		Company company = opportunity.getCompany();
		desc = company.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(desc);
		sb.append("</div>");

		sb.append("<br/>");
		sb.append("<small style='" + "position:absolute;"
				+ "right:4; bottom:2; " + "display:block;" + "'>");

		// ��ʾ����ʱ�䣬������
		// Date date = doc.get_cdate();
		// sb.append(String.format(Utils.FORMATE_DATE_FULL, date));

		AccountInfo ca = opportunity.get_caccount();
		if (ca != null) {
			sb.append(" "); //$NON-NLS-1$
			sb.append(ca.getUserName());
		}

		sb.append("</small>"); //$NON-NLS-1$

		List<Opportunity> input = (List<Opportunity>) viewer.getInput();
		if (input != null) {
			// �ж������ǰ�����һ��������ʾ�ָ���
			int i;
			for (i = 0; i < input.size(); i++) {
				Opportunity op = input.get(i);
				if (opportunity.get_id().equals(op.get_id())) {
					break;
				}
			}
			if (i != input.size() - 1 || input.size() < 4) {
				sb.append(HtmlUtil.createBottomLine(0)); //$NON-NLS-1$
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
	

	private String getHTMLSingleLine() {
		// ����desc
		Company company = opportunity.getCompany();
		String desc = Utils.getPlainText(company.getDesc());

		// ---------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer; border-bottom:1px dotted #cdcdcd;height=100%'>");

		sb.append("<span style='"//$NON-NLS-1$
				+ "font-family:΢���ź�;"//$NON-NLS-1$
				+ "font-size:10pt;"//$NON-NLS-1$
				+ "margin:0 2;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 120
				+ "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</span>");

		sb.append("</div>");

		return sb.toString();
	}

}
