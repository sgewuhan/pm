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
	public int finished_cost_normal = 10;
	
	/**
	 * Ͷ�루�з��ɱ����й�,��Ԥ����ɵ���Ŀ����
	 * TODO
	 */
	public int finished_cost_over = 15;
	
	/**
	 * ���Ʋ��ᳬ֧�Ľ�������Ŀ����
	 */
	public int processing_cost_normal = 40;

	/**
	 * ���ƻᳬ��Ԥ�����Ŀ����
	 */
	public int processing_cost_over = 10;
	
	/**
	 * ��Ŀ����Ԥ����
	 */
	public long total_budget_amount = 12128900;
	
	/**
	 * ��Ŀ�����з��ɱ����
	 */
	public long total_investment_amount = 9028374;
	
	
	
	
	
//	/**
//	 * �����Ŀ����������
//	 */
//	public int finished_sales_revenue = 10;
//	
//	/**
//	 * �����Ŀ�����۳ɱ�
//	 */
//	public int finished_sales_cost = 15;
//	
//	/**
//	 * ��������Ŀ����������
//	 */
//	public int processing_sales_revenue = 40;
//
//	/**
//	 * ��������Ŀ�����۳ɱ�
//	 */
//	public int processing_sales_cost = 10;
	
	
	
	/**
	 * ��Ŀ������������
	 */
	public long total_sales_revenue = 12128900;
	
	/**
	 * ��Ŀ�������۳ɱ�
	 */
	public long total_sales_cost = 9028374;
	
	
	
	
	
	

	public List<ProjectProvider> subOrganizationProjectProvider = new ArrayList<ProjectProvider>();
	
	public List<ProjectProvider> subChargerProjectProvider = new ArrayList<ProjectProvider>();



	
}
