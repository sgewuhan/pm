package com.sg.business.visualization.labelprovide;

import java.math.BigDecimal;

import com.sg.business.model.Project;

public class RevenueLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		double[] data = project.getSalesSummaryData();

		// ��Ŀ����������
		double salesIncome = data[0];

		// ��Ŀ�����۳ɱ�
		double salesCostValue = data[1];

		// ��Ŀ����������
		double salesRevenue = salesIncome - salesCostValue;

		double max = Math.max(Math.abs(salesIncome),
				Math.max(Math.abs(salesCostValue), Math.abs(salesRevenue)));

		if (max == 0) {
			return "";
		}

		int rateIncome = new BigDecimal(Math.abs(10 * salesIncome / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateCost = new BigDecimal(Math.abs(10 * salesCostValue / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateRevenue = new BigDecimal(Math.abs(10 * salesRevenue / max))
		.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

		StringBuffer sb = new StringBuffer();
		// ������������
		for (int i = 0; i < rateIncome - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "blue",
					"9%", null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesIncome));
		sb.append("<br/>");

		// �������۳ɱ�
		for (int i = 0; i < rateCost - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "yellow",
					"9%", null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesCostValue));
		sb.append("<br/>");

		// ��������
		for (int i = 0; i < rateRevenue - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3,
					salesIncome > salesCostValue ? "green" : "red", "9%",
					null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesRevenue));

		return sb.toString();
	}

}
