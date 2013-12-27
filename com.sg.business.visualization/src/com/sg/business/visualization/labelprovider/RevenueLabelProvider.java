package com.sg.business.visualization.labelprovider;

import java.math.BigDecimal;

import com.sg.business.model.Project;
import com.sg.business.model.ProjectProvider;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.etl.TinyVisualizationUtil;

public class RevenueLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		// ��Ŀ����������
		double salesRevenue = pres.getSalesRevenue();

		// ��Ŀ�ĳɱ�
		double totalCost = pres.getSalesCost();// +pres.getInvestment();

		// ��Ŀ����������
		double salesProfit = salesRevenue - totalCost;

		double max = Math.max(Math.abs(salesRevenue),
				Math.max(Math.abs(totalCost), Math.abs(salesProfit)));

		if (max == 0) {
			return ""; //$NON-NLS-1$
		}

		int rateIncome = new BigDecimal(Math.abs(10 * salesRevenue / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateCost = new BigDecimal(Math.abs(10 * totalCost / max)).setScale(
				0, BigDecimal.ROUND_HALF_UP).intValue();
		int rateRevenue = new BigDecimal(Math.abs(10 * salesProfit / max))
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

		StringBuffer sb = new StringBuffer();
		// ������������
		for (int i = 0; i < rateIncome - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "blue", "9%", //$NON-NLS-1$ //$NON-NLS-2$
					null, null, null, "14"); //$NON-NLS-1$
			sb.append(bar);
		}
		sb.append(getCurrency(salesRevenue, 7));
		sb.append("<br/>"); //$NON-NLS-1$

		// �������۳ɱ�
		for (int i = 0; i < rateCost - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3, "yellow", //$NON-NLS-1$
					"9%", null, null, null, "14"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append(bar);
		}
		sb.append(getCurrency(totalCost, 7));
		sb.append("<br/>"); //$NON-NLS-1$

		// ��������
		for (int i = 0; i < rateRevenue - 1; i++) {
			String bar = TinyVisualizationUtil.getColorBar(i + 3,
					salesRevenue > totalCost ? "green" : "red", "9%", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					null, null, null, "14"); //$NON-NLS-1$
			sb.append(bar);
		}
		sb.append(getCurrency(salesProfit, 7));

		return sb.toString();
	}

	@Override
	public String getSummary(ProjectProvider data) {
		data.getData();
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='"//$NON-NLS-1$
				+ "color:#6f6f6f;"//$NON-NLS-1$
				+ "font-style:italic;"//$NON-NLS-1$
				+ "font-family:΢���ź�;"//$NON-NLS-1$
				//				+ "font-weight:bold;"//$NON-NLS-1$
				+ "font-size:9pt;"//$NON-NLS-1$
				+ "margin-left:2;"//$NON-NLS-1$
				+ "margin-top:8;"//$NON-NLS-1$
//				+ "text-align:center;"//$NON-NLS-1$
				+ "word-break:break-all; "//$NON-NLS-1$
				+ "white-space:normal; "//$NON-NLS-1$
				+ "display:block;"//$NON-NLS-1$
				+ "'>"); //$NON-NLS-1$
		sb.append("ӯ��/����:");
		sb.append(data.sum.profit_surplus);
		sb.append("/");
		sb.append(data.sum.profit_deficit);
		sb.append("<br/>");
		sb.append("��������/��������:");
		sb.append(new BigDecimal(data.sum.total_sales_revenue/10000
				- data.sum.total_sales_cost/10000).setScale(0,
				BigDecimal.ROUND_HALF_UP).intValue());
		sb.append("/");
		sb.append(new BigDecimal(data.sum.total_sales_revenue/10000).setScale(0,
				BigDecimal.ROUND_HALF_UP).intValue());
		sb.append("</span>");//$NON-NLS-1$
		return sb.toString();
	}
}
