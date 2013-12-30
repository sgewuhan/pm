package com.sg.business.visualization.label.projectset;

import com.sg.business.model.Project;

public class InvestmentLabelProvider extends AbstractProjectLabelProvider {
	@Override
	protected String getProjectText(Project project) {

		double value = project.getPresentation().getDesignatedInvestment();

		String bv = value == 0d ? "" :String.format("%.2f",value / 10000); //$NON-NLS-1$ //$NON-NLS-2$

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-weight:bold;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>"); //$NON-NLS-1$
		sb.append(bv);
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}
}
