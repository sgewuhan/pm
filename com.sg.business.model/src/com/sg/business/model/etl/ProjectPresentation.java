package com.sg.business.model.etl;

import com.mongodb.DBObject;
import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectSetSummaryData;

public class ProjectPresentation implements IProjectETL {

	private Project project;

	public ProjectPresentation(Project project) {
		this.project = project;

	}


	/**
	 * 项目已经延期，当前的时间已经超过了项目的计划完成时间
	 * 
	 * @return
	 */
	public boolean isDelayDefinited() {
		return Boolean.TRUE.equals(getETLValue(F_IS_DELAY_DEFINITED));
	}

	public boolean isAdvanceDefinited() {
		return Boolean.TRUE.equals(getETLValue(F_IS_ADVANCE_DEFINITED));
	}

	public boolean isDelayEstimated() {
		return Boolean.TRUE.equals(getETLValue(F_IS_DELAY_ESTIMATED));
	}

	public boolean isAdvanceEstimated() {
		return Boolean.TRUE.equals(getETLValue(F_IS_ADVANCE_ESTIMATED));
	}

	/**
	 * 截至目前按照某比例估算是否可能超支
	 * 
	 * @return
	 */
	public boolean isOverCostEstimated() {
		return Boolean.TRUE.equals(getETLValue(F_IS_OVERCOST_ESTIMATED));
	}

	/**
	 * 项目是否超支
	 * 
	 * @return
	 */
	public boolean isOverCostDefinited() {
		return Boolean.TRUE.equals(getETLValue(F_IS_OVERCOST_DEFINITED));
	}

	public double getBudgetValue() {
		Double value = (Double) getETLValue(F_BUDGET);
		return value == null ? 0 : value.doubleValue();
	}

	/**
	 * 获得项目截至当前的投资总额（研发成本）
	 * 
	 * @return
	 */
	public double getInvestment() {
		Double value = (Double) getETLValue(F_INVESTMENT);
		return value == null ? 0 : value.doubleValue();
	}

	public double getAllocatedInvestment() {
		Double value = (Double) getETLValue(F_INVESTMENT_ALLOCATED);
		return value == null ? 0 : value.doubleValue();
	}

	/**
	 * @return 返回指定到工作令号的研发成本
	 */
	public double getDesignatedInvestment() {
		Double value = (Double) getETLValue(F_INVESTMENT_DESIGNATED);
		return value == null ? 0 : value.doubleValue();
	}

	public double getFinishedDurationRatio() {
		Double value = (Double) getETLValue(F_FINISHED_DURATION_RATIO);
		return value == null ? 0 : value.doubleValue();
	}

	public double getSalesRevenue() {
		Double value = (Double) getETLValue(F_SALES_REVENUE);
		return value == null ? 0 : value.doubleValue();
	}

	public double getSalesCost() {
		Double value = (Double) getETLValue(F_SALES_COST);
		return value == null ? 0 : value.doubleValue();
	}

	public String getDescriptionText() {
		return (String) getETLValue(F_DESC_TEXT);
	}

	public String getCoverImageURL() {
		return (String) getETLValue(F_COVERIMAGE_URL);
	}

	public String getLaunchOrganizationText() {
		return (String) getETLValue(F_LAUNCH_ORGANIZATION_TEXT);
	}

	public String getChargerText() {
		return (String) getETLValue(F_CHARGER_TEXT);
	}

	public String getSchedualHTMLLabel() {
		return (String) getETLValue(F_SCHEDUAL_HTML);
	}

	public String getSchedualDetailHTMLLabel() {
		return (String) getETLValue(F_SCHEDUAL_DETAIL_HTML);
	}

	public void loadSummary(ProjectSetSummaryData sum) {
		if (ILifecycle.STATUS_FINIHED_VALUE
				.equals(project.getLifecycleStatus())) {
			sum.finished++;
			if (isDelayDefinited()) {
				sum.finished_delay++;
			} else if (isAdvanceDefinited()) {
				sum.finished_advance++;
			} else {
				sum.finished_normal++;
			}
			if (isOverCostDefinited()) {
				sum.finished_cost_over++;
			} else {
				sum.finished_cost_normal++;
			}
		} else if (ILifecycle.STATUS_WIP_VALUE.equals(project
				.getLifecycleStatus())) {
			sum.processing++;
			if (isDelayEstimated()) {
				sum.processing_delay++;
			} else if (isAdvanceEstimated()) {
				sum.processing_advance++;
			} else {
				sum.processing_normal++;
			}

			if (isOverCostEstimated()) {
				sum.processing_cost_over++;
			} else {
				sum.processing_cost_normal++;
			}
		}

		sum.total_budget_amount += getBudgetValue();
		sum.total_investment_amount += getInvestment();
		sum.total_sales_revenue += getSalesRevenue();
		sum.total_sales_cost += getSalesCost();
	}

	private Object getETLValue(String field) {
		Object etl = project.getValue(F_ETL);
		if (etl instanceof DBObject) {
			return ((DBObject) etl).get(field);
		} else {
			return null;
		}
	}
}
