package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.fields.value.IFileBase;

public class FolderDistributeFileBase implements IFileBase {

	public FolderDistributeFileBase() {
	}

	@Override
	public String getDB(PrimaryObject doc) {
		String userId = new CurrentAccountContext().getConsignerId();
		User user = UserToolkit.getUserById(userId);
		Organization org = user.getOrganization();
		Organization container = org.getContainerOrganization();
		
		return container.getFileBase();
	}

	@Override
	public String getNamespace(PrimaryObject doc) {
		return null;
	}

}
