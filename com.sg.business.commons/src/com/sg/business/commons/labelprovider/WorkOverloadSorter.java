package com.sg.business.commons.labelprovider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.sorter.PrimaryObjectSortorFactory;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class WorkOverloadSorter extends PrimaryObjectSortorFactory {

	@Override
	protected Object getValue(PrimaryObject po, ColumnConfigurator configurator) {
		Work work = (Work) po;
		try {
			return work.getOverloadCount();
		} catch (Exception e) {
		}
		return -1;
	}
}
