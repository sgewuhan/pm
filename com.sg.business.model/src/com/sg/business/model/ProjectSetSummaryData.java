package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProjectSetSummaryData {

	private static final String F_FINISHED = "finished";

	private static final String F_FINISHED_ADVANCE = "finished_advance";

	private static final String F_FINISHED_COST_NORMAL = "finished_cost_normal";

	private static final String F_FINISHED_COST_OVER = "finished_cost_over";

	private static final String F_FINISHED_DELAY = "finished_delay";

	private static final String F_FINISHED_NORMAL = "finished_normal";

	private static final String F_PROCESSING = "processing";

	private static final String F_PROCESSING_ADVANCE = "processing_advance";

	private static final String F_PROCESSING_COST_NORMAL = "processing_cost_normal";

	private static final String F_PROCESSING_COST_OVER = "processing_cost_over";

	private static final String F_PROCESSING_DELAY = "processing_delay";

	private static final String F_PROCESSING_NORMAL = "processing_normal";

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
	

	public DBObject getData() {
		BasicDBObject data = new BasicDBObject();
		data.put(F_FINISHED, finished);
		data.put(F_FINISHED_ADVANCE, finished_advance);
		data.put(F_FINISHED_COST_NORMAL, finished_cost_normal);
		data.put(F_FINISHED_COST_OVER, finished_cost_over);
		data.put(F_FINISHED_DELAY, finished_delay);
		data.put(F_FINISHED_NORMAL, finished_normal);
		
		data.put(F_PROCESSING, processing);
		data.put(F_PROCESSING_ADVANCE, processing_advance);
		data.put(F_PROCESSING_COST_NORMAL, processing_cost_normal);
		data.put(F_PROCESSING_COST_OVER, processing_cost_over);
		data.put(F_PROCESSING_DELAY, processing_delay);
		data.put(F_PROCESSING_NORMAL, processing_normal);
		
		return data;
	}


	public void clean() {
		total = 0;
		finished = 0;
		processing = 0;
		processing_normal = 0;
		processing_delay = 0;
		processing_advance = 0;
		finished_normal = 0;
		finished_delay = 0;
		finished_advance = 0;
		finished_cost_normal = 0;
		finished_cost_over  = 0;
		processing_cost_normal = 0;
		processing_cost_over  = 0;
		total_budget_amount = 0;
		total_investment_amount = 0;
		total_sales_revenue = 0;
		total_sales_cost = 0;
		subOrganizationProjectProvider.clear();
		subChargerProjectProvider.clear();
	}



	
}
