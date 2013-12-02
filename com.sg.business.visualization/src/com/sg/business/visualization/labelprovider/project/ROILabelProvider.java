package com.sg.business.visualization.labelprovider.project;

import java.math.BigDecimal;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;

public class ROILabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:8pt;'>");
		// ��ĩ
		double[] salesdata = project.getSalesSummaryData();

		// ��ĩ����
		double profit = salesdata[0] - salesdata[1];
		double inv = project.getInvestment();

		if (profit > 0 && inv > 0) {
			sb.append("<span style='color="
					+ Utils.COLOR_BLUE[10]
							+ ";font-size:9pt;margin-left:0;"
							+ "word-break : break-all; white-space:normal; display:block; text-align:left;'>");
			double value = new BigDecimal(100 * profit / salesdata[0]).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			sb.append("����������: ");
			sb.append(value);
			sb.append("%");
			sb.append("</span>");


			sb.append("<span style='color="
					+ Utils.COLOR_YELLOW[10]
							+ ";font-size:9pt;margin-left:0;"
							+ "word-break : break-all; white-space:normal; display:block; text-align:left;'>");
			value = new BigDecimal(100 * profit / salesdata[1]).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			sb.append("�ɱ�������: ");
			sb.append(value);
			sb.append("%");
			sb.append("</span>");

			value = new BigDecimal(100 * profit / inv).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			sb.append("<span style='color="
					+ Utils.COLOR_GREEN[10]
							+ ";font-size:9pt;margin-left:0;"
							+ "word-break : break-all; white-space:normal; display:block; text-align:left;'>");
			sb.append("ROI: ");
			sb.append(value);
			sb.append("%");
			sb.append("</span>");
			
		}
		
		
//		sb.append("��������:");
//		sb.append(getCurrency(profit));
//		sb.append("<br/>");
//		sb.append("�з�Ͷ��:");
//		sb.append(getCurrency(inv));
//		sb.append("<br/>");

		sb.append("</span>");
		return sb.toString();
	}

}
