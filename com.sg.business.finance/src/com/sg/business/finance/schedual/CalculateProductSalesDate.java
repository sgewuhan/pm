package com.sg.business.finance.schedual;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.sg.business.model.toolkit.ProjectToolkit;

public class CalculateProductSalesDate implements ISchedualJobRunnable {


	@Override
	public boolean run() throws Exception {
		Commons.loginfo("[��������]��ʼ���²�Ʒ��������");
		ProjectToolkit.updateProjectSalesData();
		Commons.loginfo("[��������]��Ʒ�������ݸ������");
		return true;
	}

}
