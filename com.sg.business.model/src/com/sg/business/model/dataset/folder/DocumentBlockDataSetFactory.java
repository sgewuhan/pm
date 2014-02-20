package com.sg.business.model.dataset.folder;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.sg.business.model.IModelConstants;

public class DocumentBlockDataSetFactory extends
		SingleDBCollectionDataSetFactory {

	public DocumentBlockDataSetFactory() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

}
