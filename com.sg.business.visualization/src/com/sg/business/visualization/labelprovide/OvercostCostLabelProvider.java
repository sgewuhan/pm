package com.sg.business.visualization.labelprovide;

import java.math.BigDecimal;

import com.sg.business.model.Project;

public class OvercostCostLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:left;'>");

		Double budgetValue = project.getBudgetValue();
		double dr = project.getDurationFinishedRatio();

		double investment = project.getInvestment();
		if (budgetValue == null) {
			sb.append("--");
		} else {
			sb.append("Ԥ��:");
			int ratio = new BigDecimal(100 * investment / budgetValue)
					.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			
			sb.append(ratio);
			sb.append("%");

			if(dr != 0){
				sb.append("<br/>");
				int ratio2 = new BigDecimal(100 * dr)
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				sb.append("����:");
				sb.append(ratio2);
				sb.append("%");
			}else{
				
			}
		}
		sb.append("</span>");
		return sb.toString();
	}

}
