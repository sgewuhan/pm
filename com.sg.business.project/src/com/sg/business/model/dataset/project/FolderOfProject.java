package com.sg.business.model.dataset.project;

import com.mongodb.BasicDBObject;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

public class FolderOfProject extends MasterDetailDataSetFactory {

	public FolderOfProject() {
		super(IModelConstants.DB, IModelConstants.C_FOLDER);
		setQueryCondition(new BasicDBObject().append(Folder.F_PARENT_ID, null));
	}

	@Override
	protected String getDetailCollectionKey() {
		return Folder.F_PROJECT_ID;
	}

}
