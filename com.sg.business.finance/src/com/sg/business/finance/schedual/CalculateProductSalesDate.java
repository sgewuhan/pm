package com.sg.business.finance.schedual;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.sg.business.model.toolkit.ProjectToolkit;

public class CalculateProductSalesDate implements ISchedualJobRunnable {


	@Override
	public boolean run() throws Exception {
		Commons.LOGGER.info("[��������]��ʼ���²�Ʒ��������");
		ProjectToolkit.updateProjectSalesData();
		Commons.LOGGER.info("[��������]��Ʒ�������ݸ������");
		return true;
	}

}
