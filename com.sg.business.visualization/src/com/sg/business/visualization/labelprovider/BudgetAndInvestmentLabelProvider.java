package com.sg.business.visualization.labelprovider;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.business.model.etl.TinyVisualizationUtil;
import com.sg.business.visualization.nls.Messages;

public class BudgetAndInvestmentLabelProvider extends
		AbstractProjectLabelProvider {
	private static final int num = 6;// ����Ϊ5��

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);

		// ��Ŀ��Ԥ��
		double budgetValue = pres.getBudgetValue();
		String bv = (budgetValue == 0d) ? "--" : (nf //$NON-NLS-1$
				.format(budgetValue / 10000));

		// ��Ŀ���з��ɱ�
		double investment = pres.getDesignatedInvestment();
		String iv = (investment == 0d) ? "0" //$NON-NLS-1$
				: (nf.format(investment / 10000));

		StringBuffer sb = new StringBuffer();
		if (budgetValue != 0) {
			// ��õ���ɱ���
			double ratio = investment / budgetValue;
			double _d = ratio > 1 ? 1 / ratio : ratio;
			int scale = new BigDecimal(_d * num).setScale(0,
					BigDecimal.ROUND_HALF_UP).intValue();

			// ����Ԥ����
			if (ratio > 1) {// ��֧��
				for (int i = 0; i < scale; i++) {
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"blue", "16%", null, null, null, "14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			} else {
				for (int i = 0; i < num; i++) {
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"blue", "16%", null, null, null, "14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			}

			sb.append("<br/>"); //$NON-NLS-1$

			// ����Ԥ����
			if (ratio > 1) {// ��֧��
				for (int i = 0; i < num; i++) {
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"red", "16%", null, null, null, "14"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			} else {
				for (int i = 0; i < scale; i++) {
					String bar = TinyVisualizationUtil.getColorBar(i + 3,
							"green", "16%", null, null, null, "10"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					sb.append(bar);
				}
			}

		}

		sb.append("<span style='FONT-FAMILY:΢���ź�;font-size:8pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>"); //$NON-NLS-1$
		sb.append(Messages.get().BudgetAndInvestmentLabelProvider_0);
		sb.append(iv);
		sb.append("/"); //$NON-NLS-1$
		sb.append(bv);

		if (budgetValue != 0d) {
			sb.append(" "); //$NON-NLS-1$
			int ratio = new BigDecimal(100 * investment / budgetValue)
					.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			sb.append(ratio);
			sb.append("%"); //$NON-NLS-1$
		}
		sb.append(" "); //$NON-NLS-1$

		// ����״̬
		if (budgetValue == 0d) {

		} else if (budgetValue < investment) {
			sb.append("<span style='color:" + Utils.COLOR_RED[10] + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
			sb.append(Messages.get().BudgetAndInvestmentLabelProvider_1);
			sb.append("</span>"); //$NON-NLS-1$
		} else {
			boolean maybeOverCost = pres.isOverCostEstimated();
			if (maybeOverCost) {
				sb.append("<span style='color:" + Utils.COLOR_YELLOW[10] + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
				sb.append(Messages.get().BudgetAndInvestmentLabelProvider_2);
				sb.append("</span>"); //$NON-NLS-1$
			}
		}
		sb.append("</span>"); //$NON-NLS-1$

		return sb.toString();
	}
}
