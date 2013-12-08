package com.sg.business.visualization.labelprovide;

import java.math.BigDecimal;

import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.etl.TinyVisualizationUtil;

public class RevenueLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		// 项目的销售收入
		double salesRevenue = pres.getSalesRevenue();

		// 项目的销售成本
		double salesCostValue = pres.getSalesCost();

		// 项目的销售利润
		double salesProfit = salesRevenue - salesCostValue;

		double max = Math.max(Math.abs(salesRevenue),
				Math.max(Math.abs(salesCostValue), Math.abs(salesProfit)));

		if (max == 0) {
			return "";
		}

		int rateIncome = new BigDecimal(Math.abs(10 * salesRevenue / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateCost = new BigDecimal(Math.abs(10 * salesCostValue / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateRevenue = new BigDecimal(Math.abs(10 * salesProfit / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

		StringBuffer sb = new StringBuffer();
		// 绘制销售收入
		for (int i = 0; i < rateIncome - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "blue", "9%",
					null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesRevenue));
		sb.append("<br/>");

		// 绘制销售成本
		for (int i = 0; i < rateCost - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "yellow",
					"9%", null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesCostValue));
		sb.append("<br/>");

		// 绘制利润
		for (int i = 0; i < rateRevenue - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3,
					salesRevenue > salesCostValue ? "green" : "red", "9%",
					null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesProfit));

		return sb.toString();
	}

}
