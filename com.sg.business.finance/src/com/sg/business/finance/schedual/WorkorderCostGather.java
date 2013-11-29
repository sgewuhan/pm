package com.sg.business.finance.schedual;

import java.util.Calendar;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.sg.business.finance.eai.WorkorderPeriodCostAdapter;
import com.sg.business.model.CompanyWorkOrder;
import com.sg.business.model.CostAccount;

/**
 * 每月1号对上月的数据进行获取
 * 
 * @author Administrator
 * 
 */
public class WorkorderCostGather implements ISchedualJobRunnable {

	@Override
	public boolean run() throws Exception {
		WorkorderPeriodCostAdapter adapter = new WorkorderPeriodCostAdapter();

		Calendar cal = Calendar.getInstance();
		for (int i = 0; i > -23; i--) {
			cal.add(Calendar.MONTH, -1);

			long start = System.currentTimeMillis();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;

			String[] costElementArray = CostAccount.getCostElemenArray();
			String[] workOrders = CompanyWorkOrder.getWorkOrders();
			try {
				Commons.LOGGER.info("[成本数据]准备获取SAP工作令号研发成本数据:" + year + "-"
						+ month);
				adapter.runGetData(workOrders, costElementArray, year, month);
			} catch (Exception e) {
				Commons.LOGGER.error("[成本数据]获得SAP工作令号研发成本失败:" + year + "-"
						+ month, e);
				throw e;
			}
			long end = System.currentTimeMillis();
			Commons.LOGGER.info("[成本数据]获得SAP工作令号研发成本完成:" + year + "-" + month
					+ " " + (end - start) / 1000+"S");
		}

		return true;

	}
}
