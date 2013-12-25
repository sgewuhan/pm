package com.sg.business.model.etl;

import java.util.Calendar;
import java.util.Iterator;

import com.mobnut.db.DBActivator;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.CostAccount;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProductItem;
import com.sg.business.model.Project;

public class ProjectMonthlyETL extends ProjectETL {

	public ProjectMonthlyETL(Project project) {
		super(project);
	}

	private double monthAllocatedInvestment;

	private double monthDesignatedInvestment;

	private double monthInvestment;

	private double monthSalesRevenue;

	private double monthSalesCost;

	private double monthSalesProfit;

	/**
	 * ��÷�̯��������ŵĳɱ�
	 * 
	 * @param colname
	 * @return
	 */
	private double extractMonthlyInvestmentInternal(String colname, Calendar cal) {
		// ��ù������
		String[] workOrders = project.getWorkOrders();
		double summary = 0d;
		if (workOrders.length != 0) {
			DBCollection col = DBActivator.getCollection(IModelConstants.DB,
					colname);
			DBObject matchCondition = new BasicDBObject();
			matchCondition.put(Project.F_WORK_ORDER,
					new BasicDBObject().append("$in", workOrders));
			matchCondition.put(IProjectETL.F_YEAR, cal.get(Calendar.YEAR));
			matchCondition
					.put(IProjectETL.F_MONTH, cal.get(Calendar.MONTH) + 1);
			DBObject match = new BasicDBObject().append("$match",
					matchCondition);
			DBObject groupCondition = new BasicDBObject();
			groupCondition.put("_id", "$" + Project.F_WORK_ORDER);// ��������ŷ���
			String[] costElements = CostAccount.getCostElemenArray();
			for (int i = 0; i < costElements.length; i++) {
				groupCondition.put(
						costElements[i],
						new BasicDBObject().append("$sum", "$"
								+ costElements[i]));
			}
			DBObject group = new BasicDBObject().append("$group",
					groupCondition);
			AggregationOutput agg = col.aggregate(match, group);
			Iterator<DBObject> iter = agg.results().iterator();
			while (iter.hasNext()) {
				DBObject data = iter.next();
				for (int i = 0; i < costElements.length; i++) {
					Number value = (Number) data.get(costElements[i]);
					summary += value.doubleValue();
				}
			}
		}
		return summary;
	}

	public DBObject doETL(Calendar cal) throws Exception {
		DBObject etl = super.doETL(cal);
		// ��Ŀ�������ֶ�
		etl.put(Project.F_CHARGER, project.getValue(Project.F_CHARGER));
		// ��Ŀ���������ֶ�
		etl.put(Project.F_BUSINESS_CHARGER, project.getValue(Project.F_BUSINESS_CHARGER));
		// ��Ŀ��������
		etl.put(Project.F_BUSINESS_ORGANIZATION, project.getValue(Project.F_BUSINESS_ORGANIZATION));
		// ���������ֶ�
		etl.put(Project.F_PARTICIPATE, project.getValue(Project.F_PARTICIPATE));
		// ��Ŀ��������Ŀְ����֯
		etl.put(Project.F_FUNCTION_ORGANIZATION,
				project.getValue(Project.F_FUNCTION_ORGANIZATION));
		// ��Ŀ������
		etl.put(Project.F_LAUNCH_ORGANIZATION,
				project.getValue(Project.F_LAUNCH_ORGANIZATION));
		// ���ñ�׼��
		etl.put(Project.F_STANDARD_SET_OPTION,
				project.getValue(Project.F_STANDARD_SET_OPTION));
		// ��Ʒ����ѡ�
		etl.put(Project.F_PRODUCT_TYPE_OPTION,
				project.getValue(Project.F_PRODUCT_TYPE_OPTION));
		// ��Ŀ����ѡ�
		etl.put(Project.F_PROJECT_TYPE_OPTION,
				project.getValue(Project.F_PROJECT_TYPE_OPTION));
		// �б����͵��ֶΣ��������
		etl.put(Project.F_WORK_ORDER, project.getValue(Project.F_WORK_ORDER));
		// ��������״̬
		etl.put(Project.F_LIFECYCLE, project.getValue(Project.F_LIFECYCLE));
		// �ƻ���ʼʱ��
		etl.put(Project.F_PLAN_START, project.getValue(Project.F_PLAN_START));
		// �ƻ����ʱ��
		etl.put(Project.F_PLAN_FINISH, project.getValue(Project.F_PLAN_FINISH));
		// ʵ�ʿ�ʼʱ��
		etl.put(Project.F_ACTUAL_START,
				project.getValue(Project.F_ACTUAL_START));
		// ʵ�����ʱ��
		etl.put(Project.F_ACTUAL_FINISH,
				project.getValue(Project.F_ACTUAL_FINISH));
		// �ƻ���ʱ
		etl.put(Project.F_PLAN_WORKS, project.getValue(Project.F_PLAN_WORKS));
		// ʵ�ʹ�ʱ
		etl.put(Project.F_ACTUAL_WORKS,
				project.getValue(Project.F_ACTUAL_WORKS));
		// �ƻ�����
		etl.put(Project.F_PLAN_DURATION,
				project.getValue(Project.F_PLAN_DURATION));
		// ʵ�ʹ���
		etl.put(Project.F_ACTUAL_DURATION,
				project.getValue(Project.F_ACTUAL_DURATION));
		/**
		 * ȡ�з��ɱ�
		 */
		// ��̯��������ŵ��з��ɱ�
		monthAllocatedInvestment = extractMonthlyInvestmentInternal(
				IModelConstants.C_RND_PEROIDCOST_ALLOCATION, cal);
		etl.put(F_MONTH_INVESTMENT_ALLOCATED, monthAllocatedInvestment);

		// ָ������������з��ɱ�
		monthDesignatedInvestment = extractMonthlyInvestmentInternal(
				IModelConstants.C_WORKORDER_COST, cal);
		etl.put(F_MONTH_INVESTMENT_DESIGNATED, monthDesignatedInvestment);

		// �ϼ��з��ɱ�
		monthInvestment = monthAllocatedInvestment + monthDesignatedInvestment;
		etl.put(F_MONTH_INVESTMENT, monthInvestment);

		/**
		 * ȡ��������,�ɱ�
		 */
		monthSalesRevenue = 0d;
		monthSalesCost = 0d;
		if (products != null) {
			for (int i = 0; i < products.size(); i++) {
				ProductItem pd = (ProductItem) products.get(i);
				double[] monthlySalesData = pd.getMonthlySalesData(cal);
				monthSalesRevenue += monthlySalesData[0];
				monthSalesCost += monthlySalesData[1];
			}
		}
		monthSalesProfit = monthSalesRevenue - monthSalesCost;
		etl.put(F_MONTH_SALES_COST, monthSalesCost);
		etl.put(F_MONTH_SALES_REVENUE, monthSalesRevenue);
		etl.put(F_MONTH_SALES_PROFIT, monthSalesProfit);
		
		Calendar cal1 = Calendar.getInstance();
		
		cal1.setTime(cal.getTime());
		cal1.add(Calendar.MONTH, -1);
		etl.put(F_YEAR, cal1.get(Calendar.YEAR));
		etl.put(F_MONTH, cal1.get(Calendar.MONTH) + 1);

		return etl;
	}

}
