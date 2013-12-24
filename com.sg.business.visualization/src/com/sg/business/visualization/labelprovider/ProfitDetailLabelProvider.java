package com.sg.business.visualization.labelprovider;

import java.math.BigDecimal;

import com.mobnut.commons.util.Utils;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;

public class ProfitDetailLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		ProjectPresentation pres = project.getPresentation();

		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:Î¢ÈíÑÅºÚ;font-size:8pt;'>"); //$NON-NLS-1$
		// ÆÚÄ©
		double salesRevenue = pres.getSalesRevenue();
		double totalCost = pres.getSalesCost();// + pres.getInvestment();

		// ÆÚÄ©ÀûÈó
		double profit = salesRevenue - totalCost;

		sb.append("<span style='color=" //$NON-NLS-1$
				+ (profit > 0?Utils.COLOR_GREEN[10]:Utils.COLOR_RED[10])
				+ ";display:block; text-align:right;font-weight:bold;font-size:10pt;'>"); //$NON-NLS-1$
		sb.append(getCurrency(profit,10));
		sb.append("</span>"); //$NON-NLS-1$
		
		if (profit > 0 && salesRevenue > 0) {
			sb.append("<span style='color=" //$NON-NLS-1$
					+ Utils.COLOR_BLUE[10]
					+ ";font-size:9pt;margin-left:0;" //$NON-NLS-1$
					+ "word-break : break-all; white-space:normal; display:block; text-align:right;'>"); //$NON-NLS-1$
			double value = new BigDecimal(100 * profit / salesRevenue).setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			sb.append(value);
			sb.append("%"); //$NON-NLS-1$
			sb.append("</span>"); //$NON-NLS-1$
		}
		return sb.toString();
	}

}
