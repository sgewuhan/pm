package com.sg.business.visualization.labelprovider;

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

		// 项目的成本
		double totalCost = pres.getSalesCost()+pres.getInvestment();

		// 项目的销售利润
		double salesProfit = salesRevenue - totalCost;

		double max = Math.max(Math.abs(salesRevenue),
				Math.max(Math.abs(totalCost), Math.abs(salesProfit)));

		if (max == 0) {
			return "";
		}

		int rateIncome = new BigDecimal(Math.abs(10 * salesRevenue / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateCost = new BigDecimal(Math.abs(10 * totalCost / max))
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
		sb.append(getCurrency(salesRevenue,7));
		sb.append("<br/>");

		// 绘制销售成本
		for (int i = 0; i < rateCost - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "yellow",
					"9%", null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(totalCost,7));
		sb.append("<br/>");

		// 绘制利润
		for (int i = 0; i < rateRevenue - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3,
					salesRevenue > totalCost ? "green" : "red", "9%",
					null, null, null, "14");
			sb.append(bar);
		}
		sb.append(getCurrency(salesProfit,7));

		return sb.toString();
	}

}
