package com.sg.business.visualization.labelprovide;

import java.math.BigDecimal;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class OvercostCostLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:left;'>");

		double budgetValue = pres.getBudgetValue();
		double dr = pres.getFinishedDurationRatio();

		double investment = pres.getInvestment();
		if (budgetValue == 0) {
			sb.append("");
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
