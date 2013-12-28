package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProjectSetSummaryData {

	private static final String F_FINISHED = "finished"; //$NON-NLS-1$

	private static final String F_FINISHED_ADVANCE = "finished_advance"; //$NON-NLS-1$

	private static final String F_FINISHED_COST_NORMAL = "finished_cost_normal"; //$NON-NLS-1$

	private static final String F_FINISHED_COST_OVER = "finished_cost_over"; //$NON-NLS-1$

	private static final String F_FINISHED_DELAY = "finished_delay"; //$NON-NLS-1$

	private static final String F_FINISHED_NORMAL = "finished_normal"; //$NON-NLS-1$

	private static final String F_PROCESSING = "processing"; //$NON-NLS-1$

	private static final String F_PROCESSING_ADVANCE = "processing_advance"; //$NON-NLS-1$

	private static final String F_PROCESSING_COST_NORMAL = "processing_cost_normal"; //$NON-NLS-1$

	private static final String F_PROCESSING_COST_OVER = "processing_cost_over"; //$NON-NLS-1$

	private static final String F_PROCESSING_DELAY = "processing_delay"; //$NON-NLS-1$

	private static final String F_PROCESSING_NORMAL = "processing_normal"; //$NON-NLS-1$

	private static final String F_PROFIT_SURPLUS = "profit_surplus";

	private static final String F_PROFIT_DEFICIT = "profit_deficit";

	public int total;

	/**
	 * �����й�
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
	 * Ͷ�루�з��ɱ����й�,Ԥ������ɵ���Ŀ����
	 */
	public int finished_cost_normal;
	
	/**
	 * Ͷ�루�з��ɱ����й�,��Ԥ����ɵ���Ŀ����
	 */
	public int finished_cost_over ;
	
	/**
	 * ���Ʋ��ᳬ֧�Ľ�������Ŀ����
	 */
	public int processing_cost_normal;
	
	/**
	 * ӯ����Ŀ��
	 */
	public int profit_surplus;
	
	/**
	 * ������Ŀ��
	 */
	public int profit_deficit;

	/**
	 * ���ƻᳬ��Ԥ�����Ŀ����
	 */
	public int processing_cost_over ;
	
	/**
	 * ��Ŀ����Ԥ����
	 */
	public long total_budget_amount;
	
	
	
	/**
	 * ��Ŀ�����з��ɱ����
	 */
	public long total_investment_amount;
	
	
	/**
	 * ��Ŀ������������
	 */
	public long total_sales_revenue;
	
	/**
	 * ��Ŀ�������۳ɱ�
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
		
		data.put(F_PROFIT_SURPLUS, profit_surplus);
		data.put(F_PROFIT_DEFICIT, profit_deficit);

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
		
		profit_surplus = 0;
		profit_deficit = 0;
		
		subOrganizationProjectProvider.clear();
		subChargerProjectProvider.clear();
	}



	
}
