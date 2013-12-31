package com.sg.business.visualization.label.projectset;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class SalesCostDetailLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		// ��Ŀ��Ԥ��
		double value = pres.getSalesCost();
		
		String bv = value==0d?"":String.format("%.2f",value / 10000); //$NON-NLS-1$ //$NON-NLS-2$

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-weight:bold;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>"); //$NON-NLS-1$
		sb.append(bv);
		sb.append("</span>"); //$NON-NLS-1$
		return sb.toString();
	}

}
