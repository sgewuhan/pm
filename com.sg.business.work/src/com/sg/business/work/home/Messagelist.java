package com.sg.business.work.home;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.dataset.message.MessageSet;

public class Messagelist extends AbstractListViewSideItem{

	@Override
	protected DataSetFactory createDataSetFactory() {
		return  new MessageSet();
	}


}
