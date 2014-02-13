package com.sg.business.pm2.home;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.dataset.message.MessageSet;

public class Messagelist extends AbstractListViewSideItem{

	@Override
	protected DataSetFactory createDataSetFactory() {
		return  new MessageSet();
	}


}
