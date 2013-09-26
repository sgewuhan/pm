package com.sg.business.vault.editor;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Container;
import com.sg.business.model.Folder;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.viewer.ChildPrimaryObjectCreator;

public class FolderCreater extends ChildPrimaryObjectCreator {

	@Override
	protected String getMessageForEmptySelection() {
		return "您需要选择上级目录或者文件柜后进行创建";
	}

	@Override
	protected void setParentData(PrimaryObject po) {
		PrimaryObject parent = po.getParentPrimaryObjectCache();
		ObjectId parentId;
		ObjectId rootId;
		String containerCollection, containerDB;

		if (parent instanceof Container) {
			parentId = null;
			rootId = parent.get_id();
			containerCollection = IModelConstants.C_ORGANIZATION;
			containerDB = (String) ((Container) parent)
					.getValue(Container.F_SOURCE_DB);
		} else if (parent instanceof Folder) {
			parentId = parent.get_id();
			rootId = ((Folder) parent).getRoot_id();
			containerDB = parent.getDbName();
			containerCollection = IModelConstants.C_ORGANIZATION;
		} else {
			return;
		}
		po.setValue(Folder.F_PARENT_ID, parentId);
		po.setValue(Folder.F_ROOT_ID, rootId);
		po.setValue(Folder.F_CONTAINER_DB, containerDB);
		po.setValue(Folder.F_CONTAINER_COLLECTION, containerCollection);

	}

}
