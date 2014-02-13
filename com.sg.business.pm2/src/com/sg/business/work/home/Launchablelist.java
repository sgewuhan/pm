package com.sg.business.work.home;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.dataset.projecttemplate.LaunchableWorkSet;

public class Launchablelist extends AbstractListViewSideItem{

	@Override
	protected DataSetFactory createDataSetFactory() {
		return  new LaunchableWorkSet();
	}


}
