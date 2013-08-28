package com.sg.business.model.dataset.project;

import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class DocumentsOfProject extends MasterDetailDataSetFactory {

	public DocumentsOfProject() {
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT);
	}

	@Override
	protected String getDetailCollectionKey() {
		return Document.F_PROJECT_ID;
	}

}
