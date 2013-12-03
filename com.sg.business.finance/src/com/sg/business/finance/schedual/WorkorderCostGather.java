package com.sg.business.finance.schedual;

import java.util.Calendar;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.sg.business.finance.eai.WorkorderPeriodCostAdapter;
import com.sg.business.model.CompanyWorkOrder;
import com.sg.business.model.CostAccount;

/**
 * ÿ��1�Ŷ����µ����ݽ��л�ȡ
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
				Commons.loginfo("[�ɱ�����]׼����ȡSAP��������з��ɱ�����:" + year + "-"
						+ month);
				adapter.runGetData(workOrders, costElementArray, year, month);
			} catch (Exception e) {
				Commons.logerror("[�ɱ�����]���SAP��������з��ɱ�ʧ��:" + year + "-"
						+ month, e);
				throw e;
			}
			long end = System.currentTimeMillis();
			Commons.loginfo("[�ɱ�����]���SAP��������з��ɱ����:" + year + "-" + month
					+ " " + (end - start) / 1000+"S");
		}

		return true;

	}
}
