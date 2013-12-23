package com.sg.business.visualization.data;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.RNDPeriodCost;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectAllocatedInvestment extends MasterDetailDataSetFactory {

	public ProjectAllocatedInvestment() {
		super(IModelConstants.DB, IModelConstants.C_RND_PEROIDCOST_ALLOCATION);
		setSort(new BasicDBObject().append(RNDPeriodCost.F_YEAR, -1)
				.append(RNDPeriodCost.F_MONTH, -1));
	}

	@Override
	protected Object getMasterValue() {
		Project project = ((Project) master);
		String[] workOrders = project.getWorkOrders();
		return new BasicDBObject().append("$in", workOrders); //$NON-NLS-1$
	}

	
	
	@Override
	protected String getDetailCollectionKey() {
		return "workorder"; //$NON-NLS-1$
	}

}
