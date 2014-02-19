package com.sg.business.pm2.home;

import com.mobnut.db.model.DataSetFactory;
import com.sg.business.model.dataset.message.MessageSet;

public class Messagelist extends AbstractListViewSideItem{
	private DataSetFactory dataSet;
	
	public Messagelist(){
		super();
		dataSet = new MessageSet();
	}
	
	@Override
	protected DataSetFactory getDataSetFactory() {
		return  dataSet;
	}


}
