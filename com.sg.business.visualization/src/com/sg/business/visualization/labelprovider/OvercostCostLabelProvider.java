package com.sg.business.visualization.labelprovider;

import java.math.BigDecimal;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class OvercostCostLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>"); //$NON-NLS-1$

		double budgetValue = pres.getBudgetValue();
//		double dr = pres.getFinishedDurationRatio();

		double investment = pres.getDesignatedInvestment();
		if (budgetValue == 0 || budgetValue>=investment) {
			sb.append(""); //$NON-NLS-1$
		} else {
//			sb.append("Ô¤Ëã:");
			int ratio = new BigDecimal(100 * (investment-budgetValue) / budgetValue)
					.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			
			sb.append(ratio);
			sb.append("%"); //$NON-NLS-1$

//			if(dr != 0){
//				sb.append("<br/>");
//				int ratio2 = new BigDecimal(100 * dr)
//				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
//				sb.append("¹¤ÆÚ:");
//				sb.append(ratio2);
//				sb.append("%");
//			}else{
//				
//			}
		}
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

}
