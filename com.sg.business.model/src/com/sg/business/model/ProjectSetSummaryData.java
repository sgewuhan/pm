package com.sg.business.model;

import java.util.ArrayList;
import java.util.List;

public class ProjectSetSummaryData {

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
	 * TODO
	 */
	public int finished_cost_normal;
	
	/**
	 * Ͷ�루�з��ɱ����й�,��Ԥ����ɵ���Ŀ����
	 * TODO
	 */
	public int finished_cost_over ;
	
	/**
	 * ���Ʋ��ᳬ֧�Ľ�������Ŀ����
	 */
	public int processing_cost_normal;

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



	
}
