package com.sg.business.visualization.labelprovider.project;

import java.text.NumberFormat;

import com.sg.business.model.Project;
import com.sg.business.model.ProjectBudget;

public class RNDCostLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		//��Ŀ��Ԥ��
		ProjectBudget budget = project.getBudget();
		Double budgetValue = budget.getBudgetValue();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		String bv = (budgetValue==null)?"":(nf.format(budgetValue));
		//��Ŀ���з��ɱ�
		Double investment = project.getInvestment();
		String iv = (investment==null)?"":(nf.format(investment));
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>");
		sb.append("Ԥ��:");
		sb.append(bv+"��");
		sb.append("<br/>");
		sb.append("�з��ɱ�:");
		sb.append(iv);
		
		sb.append("</span>");
		return sb.toString();
	}
}
