package com.sg.business.visualization.label.projectset;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class RevenueDetailLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		StringBuffer sb = new StringBuffer();
		double salesRevenue = pres.getSalesRevenue();

		sb.append("<span style='display:block; text-align:right;font-weight:bold;'>"); //$NON-NLS-1$
		sb.append(getCurrency(salesRevenue,10));
		sb.append("</span>"); //$NON-NLS-1$

		return sb.toString();
	}

}
