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
	 * 投入（研发成本）有关,测试数据
	 * TODO
	 */
	public int finished_cost_normal = 10;
	
	public int finished_cost_over = 15;
	
	public int processing_cost_normal = 40;

	public int processing_cost_over = 10;
	
	/**
	 * 总预算金额
	 */
	public long total_budget_amount = 12128900;
	
	/**
	 * 总研发成本金额
	 */
	public long total_investment_amount = 9028374;
	

	public List<ProjectProvider> subOrganizationProjectProvider = new ArrayList<ProjectProvider>();
	
	public List<ProjectProvider> subChargerProjectProvider = new ArrayList<ProjectProvider>();



	
}
