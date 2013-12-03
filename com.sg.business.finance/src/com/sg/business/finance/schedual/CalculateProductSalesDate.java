package com.sg.business.finance.schedual;

import com.mobnut.admin.schedual.registry.ISchedualJobRunnable;
import com.mobnut.commons.Commons;
import com.sg.business.model.toolkit.ProjectToolkit;

public class CalculateProductSalesDate implements ISchedualJobRunnable {


	@Override
	public boolean run() throws Exception {
		Commons.loginfo("[销售数据]开始更新产品销售数据");
		ProjectToolkit.updateProjectSalesData();
		Commons.loginfo("[销售数据]产品销售数据更新完成");
		return true;
	}

}
