package com.sg.business.visualization.sorter;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Project;
import com.sg.business.model.etl.ProjectPresentation;
import com.sg.widgets.commons.sorter.PrimaryObjectSortorFactory;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class SalesProfitSorter extends PrimaryObjectSortorFactory {

	@Override
	protected Object getValue(PrimaryObject po, ColumnConfigurator configurator) {
		ProjectPresentation project = ((Project) po).getPresentation();
		return project.getSalesRevenue() - project.getSalesCost();
	}

}
