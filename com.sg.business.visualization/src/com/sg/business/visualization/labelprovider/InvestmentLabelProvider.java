package com.sg.business.visualization.labelprovider;

import com.sg.business.model.Project;

public class InvestmentLabelProvider extends AbstractProjectLabelProvider {
	@Override
	protected String getProjectText(Project project) {

		double value = project.getPresentation().getDesignatedInvestment();

		String bv = value == 0d ? "" :String.format("%.2f",value / 10000);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-weight:bold;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>");
		sb.append(bv);
		sb.append("</span>");
		return sb.toString();
	}
}
