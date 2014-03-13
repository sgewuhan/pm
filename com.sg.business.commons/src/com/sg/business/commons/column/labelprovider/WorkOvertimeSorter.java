package com.sg.business.commons.column.labelprovider;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;
import com.sg.widgets.commons.sorter.PrimaryObjectSortorFactory;
import com.sg.widgets.registry.config.ColumnConfigurator;

public class WorkOvertimeSorter extends PrimaryObjectSortorFactory {

	@Override
	protected Object getValue(PrimaryObject po, ColumnConfigurator configurator) {
		Work work = (Work) po;
		if (work.isDelayFinish()) {
			return 9;
		}
		if (work.isRemindNow()) {
			return 8;
		}
		return 0;
	}

}
