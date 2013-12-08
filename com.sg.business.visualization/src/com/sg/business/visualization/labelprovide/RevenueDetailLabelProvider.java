package com.sg.business.visualization.labelprovide;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class RevenueDetailLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		StringBuffer sb = new StringBuffer();
		double salesRevenue = pres.getSalesRevenue();

		sb.append("<span style='display:block; text-align:right;font-weight:bold;'>");
		sb.append(getCurrency(salesRevenue,10));
		sb.append("</span>");

		return sb.toString();
	}

}
