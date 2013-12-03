package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectSetSummaryData {

	public int total;

	/**
	 * 进度有关
	 */
	public int finished;

	public int processing;

	public int processing_normal;

	public int processing_delay;

	public int processing_advance;

	public int finished_normal;

	public int finished_delay;

	public int finished_advance;
	
	
	/**
	 * 投入（研发成本）有关,预算内完成的项目数量
	 * TODO
	 */
	public int finished_cost_normal;
	
	/**
	 * 投入（研发成本）有关,超预算完成的项目数量
	 * TODO
	 */
	public int finished_cost_over ;
	
	/**
	 * 估计不会超支的进行中项目数量
	 */
	public int processing_cost_normal;

	/**
	 * 估计会超过预算的项目数量
	 */
	public int processing_cost_over ;
	
	/**
	 * 项目的总预算金额
	 */
	public long total_budget_amount;
	
	/**
	 * 项目的总研发成本金额
	 */
	public long total_investment_amount;
	
	
	
	/**
	 * 项目的总销售收入
	 */
	public long total_sales_revenue;
	
	/**
	 * 项目的总销售成本
	 */
	public long total_sales_cost;
	
	

	public List<ProjectProvider> subOrganizationProjectProvider = new ArrayList<ProjectProvider>();
	
	public List<ProjectProvider> subChargerProjectProvider = new ArrayList<ProjectProvider>();



	
}
