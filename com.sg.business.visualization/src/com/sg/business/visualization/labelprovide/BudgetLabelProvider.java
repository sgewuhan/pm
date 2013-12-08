package com.sg.business.visualization.labelprovide;

import java.text.DecimalFormat;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;

public class BudgetLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2);
		
		// ÏîÄ¿µÄÔ¤Ëã
		double budgetValue = project.getPresentation().getBudgetValue();
		String bv = (budgetValue == 0d) ? "--" : (df
				.format(budgetValue / 10000));
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-weight:bold;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>");
		sb.append(bv);
		sb.append("</span>");
		return sb.toString();
	}


}
