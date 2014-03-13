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
	 * 获得一般的HTMLLabel
	 * 
	 * @return
	 */
	private String getHTMLCommmon() {
		return opportunity.getHTMLLabel();
	}

	/**
	 * 首页的列表显示
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

		sb.append("<div style='" + "font-family:微软雅黑;" + "font-size:10pt;"
				+ "margin:0 2;" + "color:#4d4d4d;" + "white-space:nowrap;"
				+ "'>"); //$NON-NLS-1$

		sb.append(desc);

		// 显示预算
		Double rev = opportunity.getBudget();
		sb.append(" <small><b>"); //$NON-NLS-1$
		sb.append("预算:");
		if (rev == null) {
			sb.append("?");
		} else {
			sb.append(new DecimalFormat(Utils.NF_NUMBER_P2).format(rev
					.doubleValue()));
			sb.append("万");
		}
		sb.append("</b></small>"); //$NON-NLS-1$

		// 显示进展
		String status = opportunity.getProgress();
		sb.append(" <small style='color:rgb(0,128,0);'>"); //$NON-NLS-1$
		sb.append(status);

		sb.append("</small>"); //$NON-NLS-1$
		sb.append("</div>");//$NON-NLS-1$

		sb.append("<div style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "'>"); //$NON-NLS-1$
		sb.append("客户:");
		Company company = opportunity.getCompany();
		desc = company.getDesc();
		desc = Utils.getPlainText(desc);
		sb.append(desc);
		sb.append("</div>");

		sb.append("<br/>");
		sb.append("<small style='" + "position:absolute;"
				+ "right:4; bottom:2; " + "display:block;" + "'>");

		// 显示创建时间，创建人
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
			// 判断如果当前是最后一个，不显示分割线
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
		// 工作desc
		Company company = opportunity.getCompany();
		String desc = Utils.getPlainText(company.getDesc());

		// ---------------------------------------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("<div style='cursor:pointer; border-bottom:1px dotted #cdcdcd;height=100%'>");

		sb.append("<span style='"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
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
