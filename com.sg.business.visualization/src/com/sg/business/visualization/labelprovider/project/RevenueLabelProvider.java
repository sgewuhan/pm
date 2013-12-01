package com.sg.business.visualization.labelprovider.project;

import java.math.BigDecimal;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;

public class RevenueLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		double[] data = project.getSalesData();

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
					"9%", null, null, null, "10");
			sb.append(bar);
		}
		sb.append(getCurrency(salesIncome));
		sb.append("<br/>");

		// �������۳ɱ�
		for (int i = 0; i < rateCost - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "yellow",
					"9%", null, null, null, "10");
			sb.append(bar);
		}
		sb.append(getCurrency(salesCostValue));
		sb.append("<br/>");

		// ��������
		for (int i = 0; i < rateRevenue - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3,
					salesIncome > salesCostValue ? "green" : "red", "9%",
					null, null, null, "10");
			sb.append(bar);
		}
		sb.append(getCurrency(salesRevenue));


		// if (budgetValue != null && budgetValue.doubleValue() != 0) {
		// // ��õ���ɱ���
		// double ratio = investment / budgetValue;
		// double _d = ratio > 1 ? 1 / ratio : ratio;
		// int scale = new BigDecimal(_d * num).setScale(0,
		// BigDecimal.ROUND_HALF_UP).intValue();
		//
		// // ����Ԥ����
		// if (ratio > 1) {// ��֧��
		// for (int i = 0; i < scale; i++) {
		// String bar = TinyVisualizationUtil.getColorBar(i + 3,
		// "blue", "16%", null, null, null, "10");
		// sb.append(bar);
		// }
		// } else {
		// for (int i = 0; i < num; i++) {
		// String bar = TinyVisualizationUtil.getColorBar(i + 3,
		// "blue", "16%", null, null, null, "10");
		// sb.append(bar);
		// }
		// }
		//
		// sb.append("<br/>");
		//
		// // ����Ԥ����
		// if (ratio > 1) {// ��֧��
		// for (int i = 0; i < num; i++) {
		// String bar = TinyVisualizationUtil.getColorBar(i + 3,
		// "red", "16%", null, null, null, "10");
		// sb.append(bar);
		// }
		// } else {
		// for (int i = 0; i < scale; i++) {
		// String bar = TinyVisualizationUtil.getColorBar(i + 3,
		// "green", "16%", null, null, null, "10");
		// sb.append(bar);
		// }
		// }
		//
		// }
		//
		// sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>");
		// sb.append("ʵ��/Ԥ�� :");
		// sb.append(iv);
		// sb.append("/");
		// sb.append(bv);
		//
		// if (budgetValue != null && budgetValue.doubleValue() != 0) {
		// sb.append(" ");
		// int ratio = new BigDecimal(100 * investment / budgetValue)
		// .setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		// sb.append(ratio);
		// sb.append("%");
		// }
		// sb.append(" ");
		//
		// // ����״̬
		// if (budgetValue == null) {
		//
		// } else if (budgetValue.doubleValue() < investment) {
		// sb.append("<span style='color=" + Utils.COLOR_RED[10] + "'>");
		// sb.append("��֧");
		// sb.append("</span>");
		// } else {
		// boolean maybeOverCost = project.maybeOverCostNow();
		// if (maybeOverCost) {
		// sb.append("<span style='color=" + Utils.COLOR_YELLOW[10] + "'>");
		// sb.append("��֧����");
		// sb.append("</span>");
		// }
		// }

		return sb.toString();
	}

	protected String getCurrency(double value) {
		if (value >= 0) {

			return "<span style='FONT-FAMILY:΢���ź�;font-size:8pt;margin-left:1;'>"
					+ new BigDecimal(value/10000).setScale(1,
							BigDecimal.ROUND_HALF_UP).doubleValue()+ "</span>";
		} else {
			return "<span style='FONT-FAMILY:΢���ź�;font-size:8pt;margin-left:1;color="
					+ Utils.COLOR_RED[10]
					+ "'>"
					+ new BigDecimal(-value/10000).setScale(1,
							BigDecimal.ROUND_HALF_UP).doubleValue() + "</span>";
		}

	}
}
