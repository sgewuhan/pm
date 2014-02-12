package com.sg.business.work.home;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.dataset.work.ProcessingNavigatorItemSet;

public class Todolist extends AbstractListViewSideItem {


	@Override
	protected DataSetFactory createDataSetFactory() {
		return new ProcessingNavigatorItemSet();
	}


}
