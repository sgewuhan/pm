package com.sg.business.model.dataset.folder;

import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class DocumentOfFolder extends MasterDetailDataSetFactory {

	public DocumentOfFolder() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Document.F_FOLDER_ID;
	}
}
