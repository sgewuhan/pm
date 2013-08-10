package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class UserDataSetFactory extends SingleDBCollectionDataSetFactory {

	public UserDataSetFactory() {
		super(IModelConstants.DB, IModelConstants.C_USER);
	}
}
