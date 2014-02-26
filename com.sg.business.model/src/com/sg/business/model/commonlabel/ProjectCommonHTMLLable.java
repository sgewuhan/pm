package com.sg.business.model.commonlabel;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;

public class ProjectCommonHTMLLable extends CommonHTMLLabel {

	private Project project;

	public ProjectCommonHTMLLable(Project project) {
		this.project = project;
	}

	@Override
	public String getHTML() {
		if (project == null) {
			return ""; //$NON-NLS-1$
		}

		if ("singleline.budget".equals(key)) {
			return getHTMLForBudgetSingleLine();
		}
		ProjectPresentation pres = project.getPresentation();

		String desc = pres.getDescriptionText();

		String schedualLabel = pres.getSchedualHTMLLabel();

		StringBuffer sb = new StringBuffer();
		sb.append(schedualLabel);
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:10pt;position:absolute; left:0; top:0; display:block;'>"); //$NON-NLS-1$
		// 显示项目名称
		sb.append(desc);
		sb.append("</span>");

		return sb.toString();
	}

	private String getHTMLForBudgetSingleLine() {
		ProjectPresentation pres = project.getPresentation();
		boolean presentationMode = pres.isETLDataAvailable();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);

		String desc;
		if (presentationMode) {
			desc = pres.getDescriptionText();
		} else {
			desc = project.getDesc();
			desc = Utils.getPlainText(desc);
		}

		// 项目的预算
		double budgetValue;
		if (presentationMode) {
			budgetValue = pres.getBudgetValue();
		} else {
			ProjectBudget projectBudget = project.getBudget();
			if (projectBudget == null) {
				budgetValue = 0d;
			} else {
				Double value = projectBudget.getBudgetValue();
				budgetValue = value == null ? 0d : value.doubleValue();
			}
		}
		String bv = (budgetValue == 0d) ? "--" : (nf //$NON-NLS-1$
				.format(budgetValue / 10000));

		// 项目的研发成本
		double investment = 0d;
		if (presentationMode) {
			investment = pres.getDesignatedInvestment();
		} else {
			investment = 0d;
		}

		String iv = (investment == 0d) ? "0" //$NON-NLS-1$
				: (nf.format(investment / 10000));
		String ratio;
		if (budgetValue != 0d) {
			int iRatio = new BigDecimal(100 * investment / budgetValue)
					.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			ratio = iRatio + "%";
		} else {
			ratio = "na";
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<div style='font-family:微软雅黑;cursor:pointer; border-bottom:1px dotted #cdcdcd;height=100%'>");

		sb.append("<span style='"//$NON-NLS-1$
				+ "font-size:10pt;"//$NON-NLS-1$
				+ "margin:0 2;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 200
				+ "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</span>");

		sb.append("<span style='" + "color:#909090;" + "font-size:8pt;"
				+ "margin:0 2;" + "width:" + 120 + "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(Messages.get().BudgetAndInvestmentLabelProvider_0);
		sb.append(iv);
		sb.append("/"); //$NON-NLS-1$
		sb.append(bv);
		sb.append("</span>"); //$NON-NLS-1$

		sb.append(" ");
		sb.append(ratio);

		// 绘制状态
		if (budgetValue == 0d) {

		} else if (budgetValue < investment) {
			sb.append("<span style='color:" + Utils.COLOR_RED[10] + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append(Messages.get().BudgetAndInvestmentLabelProvider_1);
			sb.append("</span>"); //$NON-NLS-1$
		} else {
			boolean maybeOverCost = pres.isOverCostEstimated();
			if (maybeOverCost) {
				sb.append("<span style='color:" + Utils.COLOR_YELLOW[10] + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append(Messages.get().BudgetAndInvestmentLabelProvider_2);
				sb.append("</span>"); //$NON-NLS-1$
			}
		}
		sb.append("</div>");

		return sb.toString();
	}

}
