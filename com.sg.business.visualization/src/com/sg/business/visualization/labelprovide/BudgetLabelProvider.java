package com.sg.business.visualization.labelprovide;

import com.sg.business.model.Project;

public class BudgetLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		
		// ��Ŀ��Ԥ��
		double value = project.getPresentation().getBudgetValue();
		String bv = (value == 0d) ? "" : String.format("%.2f",value / 10000);;
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-weight:bold;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>");
		sb.append(bv);
		sb.append("</span>");
		return sb.toString();
	}


}
