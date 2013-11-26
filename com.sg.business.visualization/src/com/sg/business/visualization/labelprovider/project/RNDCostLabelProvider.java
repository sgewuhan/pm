package com.sg.business.visualization.labelprovider.project;

import java.text.NumberFormat;

import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;

public class RNDCostLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		//项目的预算
		ProjectBudget budget = project.getBudget();
		Double budgetValue = budget.getBudgetValue();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		String bv = (budgetValue==null)?"":(nf.format(budgetValue));
		//项目的研发成本
		Double investment = project.getInvestment();
		String iv = (investment==null)?"":(nf.format(investment));
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>");
		sb.append("预算:");
		sb.append(bv+"万");
		sb.append("<br/>");
		sb.append("研发成本:");
		sb.append(iv);
		
		sb.append("</span>");
		return sb.toString();
	}
}
