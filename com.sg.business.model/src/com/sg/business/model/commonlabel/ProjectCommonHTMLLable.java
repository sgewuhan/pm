package com.sg.business.model.commonlabel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;

import com.mobnut.commons.html.HtmlUtil;
import com.mobnut.commons.util.Utils;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.etl.TinyVisualizationUtil;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.commons.labelprovider.CommonHTMLLabel;
import com.sg.widgets.viewer.PageListViewer;

public class ProjectCommonHTMLLable extends CommonHTMLLabel {

	private Project project;

	private static final int num = 6;

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

		if ("performence.budget".equals(key)) {
			return getHTMLForBudgetPerformenceHome();
		}
		
		if ("performence.schedure".equals(key)) {
			return getHTMLForSchedurePerformenceHome();
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

	private String getHTMLForSchedurePerformenceHome() {
		//取序号
		StructuredViewer viewer = getViewer();
		List<?> input = (List<?>) viewer.getInput();
		int index = input.indexOf(project)+1;
		if(viewer instanceof PageListViewer){
			PageListViewer plv = (PageListViewer) viewer;
			index += plv.getPageSize()*plv.getCurrentPageIndex();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<div align='center' style='"//$NON-NLS-1$
				+ "float:left;"
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:18pt;"//$NON-NLS-1$
				+ "margin:6 0 0 0;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 48
				+ "px;"
				+ "'>"); //$NON-NLS-1$
		sb.append(index);
		sb.append("</div>");
		
//		int width = getViewer().getControl().getBounds().width-48;
		sb.append("<div style='"
				+ "position:absolute; " //$NON-NLS-1$
				+ "left:50;"
				+ "right:0;"
				+ "top:10; " //$NON-NLS-1$
				+ "bottom:0; " //$NON-NLS-1$
				+ "margin:0;"
				+ "'>");

		ProjectPresentation pres = project.getPresentation();
		String desc = pres.getDescriptionText();

		sb.append(pres.getSchedualHTMLLabel());

		sb.append("<div style='"//$NON-NLS-1$
				+ "position:absolute; " //$NON-NLS-1$
				+ "left:30;"
				+ "right:0;"
				+ "top:0; " //$NON-NLS-1$
				+ "bottom:0; " //$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:10pt;"//$NON-NLS-1$
				+ "margin:0;"//$NON-NLS-1$
				+ "color:#ffffff;"//$NON-NLS-1$
				+ "width:" + 240
				+ "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		appendCharger(sb,"#ffffff","10pt");
		sb.append(desc);
		sb.append("</div>");
		
		sb.append("</div>");
		sb.append(HtmlUtil.createBottomLine(0));

		return sb.toString();
	}

	private String getHTMLForBudgetPerformenceHome() {
		//取序号
		StructuredViewer viewer = getViewer();
		List<?> input = (List<?>) viewer.getInput();
		int index = input.indexOf(project)+1;
		if(viewer instanceof PageListViewer){
			PageListViewer plv = (PageListViewer) viewer;
			index += plv.getPageSize()*plv.getCurrentPageIndex();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<div align='center' style='"//$NON-NLS-1$
				+ "float:left;"
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:18pt;"//$NON-NLS-1$
				+ "margin:6 0 0 0;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 48
				+ "px;"
				+ "'>"); //$NON-NLS-1$
		sb.append(index);
		sb.append("</div>");
		
//		int width = getViewer().getControl().getBounds().width-48;
		sb.append("<div style='"
				+ "position:absolute; " //$NON-NLS-1$
				+ "left:50;"
				+ "right:0;"
				+ "top:0; " //$NON-NLS-1$
				+ "bottom:0; " //$NON-NLS-1$
				+ "margin:0;"
				+ "'>");

		ProjectPresentation pres = project.getPresentation();
		String desc = pres.getDescriptionText();

		sb.append("<div style='"//$NON-NLS-1$
				+ "font-family:微软雅黑;"//$NON-NLS-1$
				+ "font-size:10pt;"//$NON-NLS-1$
				+ "margin:0;"//$NON-NLS-1$
				+ "color:#4d4d4d;"//$NON-NLS-1$
				+ "width:" + 200
				+ "px;"
				+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append(desc);
		sb.append("</div>");
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);

		// 项目的预算
		double budgetValue = pres.getBudgetValue();
		String bv = (budgetValue == 0d) ? "--" : (nf //$NON-NLS-1$
				.format(budgetValue / 10000));

		// 项目的研发成本
		double investment = pres.getDesignatedInvestment();
		String iv = (investment == 0d) ? "0" //$NON-NLS-1$
				: (nf.format(investment / 10000));
		
		double ammount = investment - budgetValue;
		String av = (ammount == 0d) ? "0" //$NON-NLS-1$
				: (nf.format(ammount / 10000));
		
		sb.append("<div style='FONT-FAMILY:微软雅黑;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block;'>"); //$NON-NLS-1$
//		sb.append(Messages.get().BudgetAndInvestmentLabelProvider_0);
//		sb.append(iv);
//		sb.append("/"); //$NON-NLS-1$
//		sb.append(bv);

		if (budgetValue != 0d) {
			sb.append(" "); //$NON-NLS-1$
			int ratio = new BigDecimal(100 * investment / budgetValue)
					.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			sb.append(ratio);
			sb.append("%"); //$NON-NLS-1$
		}
		sb.append(" "); //$NON-NLS-1$

		// 绘制状态
		if (budgetValue == 0d) {

		} else if (budgetValue < investment) {
			sb.append("<span style='color:" + Utils.COLOR_RED[10] + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append(Messages.get().BudgetAndInvestmentLabelProvider_1);
			sb.append(av); //$NON-NLS-1$
			sb.append("</span>"); //$NON-NLS-1$
		} else {
			boolean maybeOverCost = pres.isOverCostEstimated();
			if (maybeOverCost) {
				sb.append("<span style='color:" + Utils.COLOR_YELLOW[10] + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append(Messages.get().BudgetAndInvestmentLabelProvider_2);
				sb.append("</span>"); //$NON-NLS-1$
			}
		}
		
		appendCharger(sb,"#4d4d4d","8pt");
		sb.append("</div>"); //$NON-NLS-1$
		
		
		if (budgetValue != 0) {
			// 获得的完成比例
			double ratio = investment / budgetValue;
			double _d = ratio > 1 ? 1 / ratio : ratio;
			int scale = new BigDecimal(_d * num).setScale(0,
					BigDecimal.ROUND_HALF_UP).intValue();

			// 绘制预算条
			if (ratio > 1) {// 超支的
				for (int i = 0; i < scale; i++) {
					String stageText = (i == scale-1)?bv:null;
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"blue", "16%", null,null, stageText, "right", "14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			} else {
				for (int i = 0; i < num; i++) {
					String stageText = (i == num-1)?bv:null;
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"blue", "16%", null,null, stageText,  "right", "14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			}


			// 绘制预算条
			if (ratio > 1) {// 超支的
				for (int i = scale; i < num; i++) {
					String stageText = (i == num-1)?iv:null;
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"red", "16%", null,null, stageText,  "right", "14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			} else {
				for (int i = num; i < scale; i++) {
					String stageText = (i == scale-1)?iv:null;
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"green", "16%", null,null, stageText,  "right", "10"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			}

		}
		sb.append("</div>");
		sb.append(HtmlUtil.createBottomLine(0));

		return sb.toString();
	}

	private void appendCharger(StringBuffer sb, String color, String fontSize) {
		User charger = project.getCharger();
		if (charger != null) {
			sb.append("<span style='"//$NON-NLS-1$
					+ "font-family:微软雅黑;"//$NON-NLS-1$
					+ "font-size:"
					+ fontSize
					+ ";"//$NON-NLS-1$
					+ "margin:0 0 0 4;"//$NON-NLS-1$
					+ "color:"
					+ color
					+ ";"//$NON-NLS-1$
					+ "width:" + 80
					+ "px;"
					+ "overflow:hidden;white-space:nowrap;text-overflow:ellipsis"//$NON-NLS-1$
					+ "'>"); //$NON-NLS-1$
			sb.append(" ");
			sb.append(charger);
			Organization org = charger.getOrganization();
			if (org != null) {
				Organization forg = (Organization) org
						.getFunctionOrganization();
				if (forg != null) {
					sb.append("|");
					sb.append(forg.getSimpleName());
				}
			}			
			sb.append("</span>");
		}
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
