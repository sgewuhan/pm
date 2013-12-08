package com.sg.business.visualization.labelprovide;

import java.text.DecimalFormat;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;

public class ActualCostLabelProvider extends AbstractProjectLabelProvider {
	@Override
	protected String getProjectText(Project project) {
		DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2);

		// ��Ŀ��Ԥ��
		double value = project.getPresentation().getInvestment();
		String bv = df.format(value / 10000);

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-weight:bold;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; text-align:right;'>");
		sb.append(bv);
		sb.append("</span>");
		return sb.toString();
	}
}
