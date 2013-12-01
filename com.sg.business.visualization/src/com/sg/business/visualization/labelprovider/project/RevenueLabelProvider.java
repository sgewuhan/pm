package com.sg.business.visualization.labelprovider.project;

import java.math.BigDecimal;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;

public class RevenueLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		double[] data = project.getSalesData();

		// 项目的销售收入
		double salesIncome = data[0];

		// 项目的销售成本
		double salesCostValue = data[1];

		// 项目的销售利润
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
		// 绘制销售收入
		for (int i = 0; i < rateIncome - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "blue",
					"9%", null, null, null, "10");
			sb.append(bar);
		}
		sb.append(getCurrency(salesIncome));
		sb.append("<br/>");

		// 绘制销售成本
		for (int i = 0; i < rateCost - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "yellow",
					"9%", null, null, null, "10");
			sb.append(bar);
		}
		sb.append(getCurrency(salesCostValue));
		sb.append("<br/>");

		// 绘制利润
		for (int i = 0; i < rateRevenue - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3,
					salesIncome > salesCostValue ? "green" : "red", "9%",
					null, null, null, "10");
			sb.append(bar);
		}
		sb.append(getCurrency(salesRevenue));


		// if (budgetValue != null && budgetValue.doubleValue() != 0) {
		// // 获得的完成比例
		// double ratio = investment / budgetValue;
		// double _d = ratio > 1 ? 1 / ratio : ratio;
		// int scale = new BigDecimal(_d * num).setScale(0,
		// BigDecimal.ROUND_HALF_UP).intValue();
		//
		// // 绘制预算条
		// if (ratio > 1) {// 超支的
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
		// // 绘制预算条
		// if (ratio > 1) {// 超支的
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
		// sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>");
		// sb.append("实际/预算 :");
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
		// // 绘制状态
		// if (budgetValue == null) {
		//
		// } else if (budgetValue.doubleValue() < investment) {
		// sb.append("<span style='color=" + Utils.COLOR_RED[10] + "'>");
		// sb.append("超支");
		// sb.append("</span>");
		// } else {
		// boolean maybeOverCost = project.maybeOverCostNow();
		// if (maybeOverCost) {
		// sb.append("<span style='color=" + Utils.COLOR_YELLOW[10] + "'>");
		// sb.append("超支风险");
		// sb.append("</span>");
		// }
		// }

		return sb.toString();
	}

	protected String getCurrency(double value) {
		if (value >= 0) {

			return "<span style='FONT-FAMILY:微软雅黑;font-size:8pt;margin-left:1;'>"
					+ new BigDecimal(value/10000).setScale(1,
							BigDecimal.ROUND_HALF_UP).doubleValue()+ "</span>";
		} else {
			return "<span style='FONT-FAMILY:微软雅黑;font-size:8pt;margin-left:1;color="
					+ Utils.COLOR_RED[10]
					+ "'>"
					+ new BigDecimal(-value/10000).setScale(1,
							BigDecimal.ROUND_HALF_UP).doubleValue() + "</span>";
		}

	}
}
