package com.sg.business.model.dataset;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.widgets.part.CurrentAccountContext;

public class ContextSingleDataSetFactory extends
		SingleDBCollectionDataSetFactory {
	

	private IContext context;

	public ContextSingleDataSetFactory(String dbName, String collectionName) {
		super(dbName, collectionName);
		setContext(new CurrentAccountContext());
	}

	public void setContext(IContext context) {
		this.context = context;
	}
	
	protected IContext getContext(){
		return context;
	}

}
