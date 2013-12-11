package com.sg.business.visualization.data;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.WorkOrderPeriodCost;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectDesignateInvestment extends MasterDetailDataSetFactory {

	public ProjectDesignateInvestment() {
		super(IModelConstants.DB, IModelConstants.C_WORKORDER_COST);
		setSort(new BasicDBObject().append(WorkOrderPeriodCost.F_YEAR, -1)
				.append(WorkOrderPeriodCost.F_MONTH, -1));
	}

	@Override
	protected Object getMasterValue() {
		Project project = ((Project) master);
		String[] workOrders = project.getWorkOrders();
		return new BasicDBObject().append("$in", workOrders);
	}

	@Override
	protected String getDetailCollectionKey() {
		return WorkOrderPeriodCost.F_WORKORDER;
	}

}
