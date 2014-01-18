package com.sg.business.visualization.view;

import org.eclipse.swt.widgets.Composite;

import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;
import com.sg.business.model.etl.ProjectETL;

public class SalesListForSalesView extends AbstractSalesListView {

	@Override
	protected void drawContent(Composite parent) {
		try {
			Object[] topSalesProfit = null;
			Object[] bottomSalesProfit = null;
			Object[] topSales = null;
			Object[] bottomSales = null;
			if (selected.equals(Project.class)) {
				topSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, -1, ProjectETL.F_MONTH_SALES_PROFIT,
						ProjectETL.F_PROJECTID, year, month,"sales");
				bottomSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_PROFIT,
						ProjectETL.F_PROJECTID, year, month,"sales");

				topSales = projectProvider.getHasLastNumberTopList(limitNumber,
						-1, ProjectETL.F_MONTH_SALES_REVENUE,
						ProjectETL.F_PROJECTID, year, month,"sales");
				bottomSales = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_REVENUE,
						ProjectETL.F_PROJECTID, year, month,"sales");
			} else if (selected.equals(User.class)) {
				topSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, -1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_CHARGER, year, month,"sales");
				bottomSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_CHARGER, year, month,"sales");

				topSales = projectProvider.getHasLastNumberTopList(limitNumber,
						-1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_CHARGER, year, month,"sales");
				bottomSales = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_CHARGER, year, month,"sales");
			} else if (selected.equals(Organization.class)) {
				topSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, -1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month,"sales");
				bottomSalesProfit = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_PROFIT,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month,"sales");

				topSales = projectProvider.getHasLastNumberTopList(limitNumber,
						-1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month,"sales");
				bottomSales = projectProvider.getHasLastNumberTopList(
						limitNumber, 1, ProjectETL.F_MONTH_SALES_REVENUE,
						Project.F_LAUNCH_ORGANIZATION,
						Project.F_LAUNCH_ORGANIZATION, year, month,"sales");
			}

			topSalesProfit = removeList(topSalesProfit);
			bottomSalesProfit = removeAndSortList(bottomSalesProfit);
			topSales = removeList(topSales);
			bottomSales = removeAndSortList(bottomSales);

			profitItem.setInput(topSalesProfit, bottomSalesProfit, selected);
			revenueItem.setInput(topSales, bottomSales, selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
