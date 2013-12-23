package com.sg.business.visualization.data;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Project;
import com.sg.business.model.SalesData;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class ProjectRevenue extends MasterDetailDataSetFactory {

	public ProjectRevenue() {
		super(IModelConstants.DB, IModelConstants.C_SALESDATA);
		setSort(new BasicDBObject().append(SalesData.F_ACCOUNT_YEAR, -1)
				.append(SalesData.F_ACCOUNT_MONTH, -1));
	}

	@Override
	protected Object getMasterValue() {
		Project project = ((Project) master);
		String[] productCode = project.getProductCode();
		return new BasicDBObject().append("$in", productCode); //$NON-NLS-1$
	}

	@Override
	protected String getDetailCollectionKey() {
		return SalesData.F_MATERIAL_NUMBER;
	}

}
